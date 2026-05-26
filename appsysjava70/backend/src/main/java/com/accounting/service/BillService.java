package com.accounting.service;

import com.accounting.dto.BillDTO;
import com.accounting.entity.Bill;
import com.accounting.entity.Category;
import com.accounting.entity.Account;
import com.accounting.enums.BillType;
import com.accounting.enums.SyncStatus;
import com.accounting.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final CategoryService categoryService;
    private final AccountService accountService;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String BILL_CACHE_KEY = "bill:";
    private static final String RECENT_BILLS_KEY = "recent_bills";

    @Cacheable(value = "bills", key = "'all'")
    public List<Bill> getAllBills() {
        log.info("获取全部账单列表");
        return billRepository.findByDeletedFalseOrderByTransactionTimeDesc();
    }

    public List<Bill> getBillsByDateRange(LocalDateTime start, LocalDateTime end) {
        log.info("获取指定时间范围的账单: {} - {}", start, end);
        return billRepository.findByTransactionTimeBetweenAndDeletedFalseOrderByTransactionTimeDesc(start, end);
    }

    public List<Bill> getRecentBills(int days) {
        log.info("获取最近{}天的账单", days);
        String cacheKey = RECENT_BILLS_KEY + ":" + days;

        try {
            @SuppressWarnings("unchecked")
            List<Bill> cachedBills = (List<Bill>) redisTemplate.opsForValue().get(cacheKey);
            if (cachedBills != null) {
                log.debug("从Redis缓存获取最近{}天账单", days);
                return cachedBills;
            }
        } catch (Exception e) {
            log.warn("Redis缓存读取失败，直接查询数据库: {}", e.getMessage());
        }

        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        List<Bill> bills = billRepository.findByTransactionTimeAfterAndDeletedFalseOrderByTransactionTimeDesc(startDate);

        try {
            redisTemplate.opsForValue().set(cacheKey, bills, 1, TimeUnit.HOURS);
        } catch (Exception e) {
            log.warn("Redis缓存写入失败: {}", e.getMessage());
        }

        return bills;
    }

    public Bill getBillById(Long id) {
        log.info("根据ID获取账单: {}", id);
        String cacheKey = BILL_CACHE_KEY + id;

        try {
            Bill cachedBill = (Bill) redisTemplate.opsForValue().get(cacheKey);
            if (cachedBill != null) {
                log.debug("从Redis缓存获取账单: {}", id);
                return cachedBill;
            }
        } catch (Exception e) {
            log.warn("Redis缓存读取失败，直接查询数据库: {}", e.getMessage());
        }

        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("账单不存在: " + id));

        try {
            redisTemplate.opsForValue().set(cacheKey, bill, 30, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("Redis缓存写入失败: {}", e.getMessage());
        }

        return bill;
    }

    @Transactional
    @CacheEvict(value = "bills", allEntries = true)
    public Bill createBill(BillDTO billDTO) {
        log.info("创建账单: 金额={}, 类型={}", billDTO.getAmount(), billDTO.getType());

        if (billDTO.getClientId() != null && billRepository.existsByClientId(billDTO.getClientId())) {
            log.warn("账单已存在，跳过重复创建: clientId={}", billDTO.getClientId());
            return billRepository.findByClientIdAndDeletedFalse(billDTO.getClientId()).orElse(null);
        }

        Category category = categoryService.getCategoryById(billDTO.getCategoryId());
        Account account = accountService.getAccountById(billDTO.getAccountId());

        Bill bill = Bill.builder()
                .type(billDTO.getType())
                .amount(billDTO.getAmount())
                .category(category)
                .account(account)
                .transactionTime(billDTO.getTransactionTime())
                .merchant(billDTO.getMerchant())
                .remark(billDTO.getRemark())
                .imageUrl(billDTO.getImageUrl())
                .syncStatus(SyncStatus.SYNCED)
                .deviceId(billDTO.getDeviceId())
                .clientId(billDTO.getClientId())
                .deleted(false)
                .build();

        Bill savedBill = billRepository.save(bill);

        BigDecimal balanceChange = billDTO.getType() == BillType.INCOME
                ? billDTO.getAmount()
                : billDTO.getAmount().negate();
        accountService.updateBalance(billDTO.getAccountId(), balanceChange);

        clearRecentBillsCache();
        log.info("账单创建成功: id={}", savedBill.getId());
        return savedBill;
    }

    @Transactional
    @CacheEvict(value = "bills", allEntries = true)
    public Bill updateBill(Long id, BillDTO billDTO) {
        log.info("更新账单: {}", id);
        Bill existing = getBillById(id);

        BigDecimal oldBalanceChange = existing.getType() == BillType.INCOME
                ? existing.getAmount()
                : existing.getAmount().negate();
        accountService.updateBalance(existing.getAccount().getId(), oldBalanceChange.negate());

        Category category = categoryService.getCategoryById(billDTO.getCategoryId());
        Account account = accountService.getAccountById(billDTO.getAccountId());

        existing.setType(billDTO.getType());
        existing.setAmount(billDTO.getAmount());
        existing.setCategory(category);
        existing.setAccount(account);
        existing.setTransactionTime(billDTO.getTransactionTime());
        existing.setMerchant(billDTO.getMerchant());
        existing.setRemark(billDTO.getRemark());
        existing.setImageUrl(billDTO.getImageUrl());

        Bill savedBill = billRepository.save(existing);

        BigDecimal newBalanceChange = billDTO.getType() == BillType.INCOME
                ? billDTO.getAmount()
                : billDTO.getAmount().negate();
        accountService.updateBalance(billDTO.getAccountId(), newBalanceChange);

        try {
            redisTemplate.delete(BILL_CACHE_KEY + id);
        } catch (Exception e) {
            log.warn("Redis缓存删除失败: {}", e.getMessage());
        }
        clearRecentBillsCache();
        log.info("账单更新成功: id={}", savedBill.getId());
        return savedBill;
    }

    @Transactional
    @CacheEvict(value = "bills", allEntries = true)
    public Bill updateBillCategory(Long id, Long categoryId) {
        log.info("更新账单分类: billId={}, categoryId={}", id, categoryId);
        Bill existing = getBillById(id);
        Category category = categoryService.getCategoryById(categoryId);
        existing.setCategory(category);
        Bill savedBill = billRepository.save(existing);
        try {
            redisTemplate.delete(BILL_CACHE_KEY + id);
        } catch (Exception e) {
            log.warn("Redis缓存删除失败: {}", e.getMessage());
        }
        clearRecentBillsCache();
        return savedBill;
    }

    @Transactional
    @CacheEvict(value = "bills", allEntries = true)
    public void deleteBill(Long id) {
        log.info("删除账单: {}", id);
        Bill bill = getBillById(id);
        bill.setDeleted(true);
        billRepository.save(bill);

        BigDecimal balanceChange = bill.getType() == BillType.INCOME
                ? bill.getAmount().negate()
                : bill.getAmount();
        accountService.updateBalance(bill.getAccount().getId(), balanceChange);

        try {
            redisTemplate.delete(BILL_CACHE_KEY + id);
        } catch (Exception e) {
            log.warn("Redis缓存删除失败: {}", e.getMessage());
        }
        clearRecentBillsCache();
        log.info("账单删除成功: id={}", id);
    }

    @Transactional
    public List<Bill> syncBills(List<BillDTO> billDTOs) {
        log.info("批量同步账单: 数量={}", billDTOs.size());
        List<Bill> syncedBills = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        for (BillDTO dto : billDTOs) {
            try {
                Bill bill = createBill(dto);
                if (bill != null) {
                    syncedBills.add(bill);
                    successCount++;
                }
            } catch (Exception e) {
                log.error("同步账单失败: clientId={}, error={}", dto.getClientId(), e.getMessage());
                failCount++;
            }
        }

        log.info("账单同步完成: 成功={}, 失败={}", successCount, failCount);
        return syncedBills;
    }

    public List<Bill> getPendingSyncBills() {
        log.info("获取待同步的账单");
        return billRepository.findBySyncStatusInAndDeletedFalse(List.of(SyncStatus.PENDING, SyncStatus.FAILED));
    }

    public BigDecimal calculateTotalByTypeAndDateRange(BillType type, LocalDateTime start, LocalDateTime end) {
        return billRepository.calculateTotalByTypeAndDateRange(type, start, end);
    }

    public BigDecimal calculateTotalByTypeAndCategoryAndDateRange(BillType type, Long categoryId, LocalDateTime start, LocalDateTime end) {
        return billRepository.calculateTotalByTypeAndCategoryAndDateRange(type, categoryId, start, end);
    }

    public BigDecimal calculateAverageExpense(LocalDateTime start, LocalDateTime end) {
        BigDecimal avg = billRepository.calculateAverageExpense(start, end);
        return avg != null ? avg : BigDecimal.ZERO;
    }

    private void clearRecentBillsCache() {
        for (int days : new int[]{7, 15, 30, 90}) {
            try {
                redisTemplate.delete(RECENT_BILLS_KEY + ":" + days);
            } catch (Exception e) {
                log.warn("Redis缓存删除失败: {}", e.getMessage());
            }
        }
    }

    public LocalDateTime getStartOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    public LocalDateTime getEndOfDay(LocalDate date) {
        return date.atTime(23, 59, 59);
    }

    public LocalDateTime getStartOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
    }

    public LocalDateTime getEndOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);
    }
}
