package com.personal.accounting.service;

import com.personal.accounting.entity.VirtualAccount;
import com.personal.accounting.entity.VirtualAccountTransaction;
import com.personal.accounting.enums.FamilyRole;
import com.personal.accounting.repository.FamilyMemberRepository;
import com.personal.accounting.repository.VirtualAccountRepository;
import com.personal.accounting.repository.VirtualAccountTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VirtualAccountService {

    private final VirtualAccountRepository virtualAccountRepository;
    private final VirtualAccountTransactionRepository transactionRepository;
    private final FamilyMemberRepository familyMemberRepository;

    public List<VirtualAccount> getUserAccounts(Long userId) {
        return virtualAccountRepository.findByUserId(userId);
    }

    public List<VirtualAccount> getFamilyAccounts(Long familyId) {
        return virtualAccountRepository.findByFamilyId(familyId);
    }

    public List<VirtualAccount> getUserAndFamilyAccounts(Long userId, Long familyId) {
        return virtualAccountRepository.findByUserIdOrFamilyId(userId, familyId);
    }

    public VirtualAccount getAccountById(Long id) {
        return virtualAccountRepository.findById(id).orElseThrow(() -> new RuntimeException("虚拟账户不存在"));
    }

    @Transactional
    public VirtualAccount createAccount(VirtualAccount account, Long operatorId) {
        if (account.getIsFamilyAccount() && account.getFamilyId() != null) {
            checkFamilyAccountPermission(account.getFamilyId(), operatorId);
        }
        if (account.getCurrentBalance() == null) {
            account.setCurrentBalance(BigDecimal.ZERO);
        }
        return virtualAccountRepository.save(account);
    }

    @Transactional
    public VirtualAccount updateAccount(Long id, VirtualAccount account, Long operatorId) {
        VirtualAccount existing = getAccountById(id);
        if (existing.getIsFamilyAccount() && existing.getFamilyId() != null) {
            checkFamilyAccountPermission(existing.getFamilyId(), operatorId);
        }

        existing.setAccountName(account.getAccountName());
        existing.setTargetAmount(account.getTargetAmount());
        existing.setTargetDate(account.getTargetDate());
        existing.setPurpose(account.getPurpose());
        existing.setColor(account.getColor());
        existing.setIcon(account.getIcon());

        return virtualAccountRepository.save(existing);
    }

    @Transactional
    public void deleteAccount(Long id, Long operatorId) {
        VirtualAccount account = getAccountById(id);
        if (account.getIsFamilyAccount() && account.getFamilyId() != null) {
            checkFamilyAccountPermission(account.getFamilyId(), operatorId);
        }
        virtualAccountRepository.deleteById(id);
    }

    public List<VirtualAccountTransaction> getAccountTransactions(Long accountId) {
        return transactionRepository.findByVirtualAccountIdOrderByTransactionDateDesc(accountId);
    }

    @Transactional
    public VirtualAccountTransaction createTransaction(Long accountId, VirtualAccountTransaction transaction, Long operatorId) {
        VirtualAccount account = getAccountById(accountId);
        if (account.getIsFamilyAccount() && account.getFamilyId() != null) {
            if (!familyMemberRepository.existsByFamilyIdAndUserId(account.getFamilyId(), operatorId)) {
                throw new RuntimeException("无权限操作此家庭账户");
            }
        } else if (!account.getUserId().equals(operatorId)) {
            throw new RuntimeException("无权限操作此账户");
        }

        transaction.setVirtualAccountId(accountId);
        transaction.setUserId(operatorId);

        VirtualAccountTransaction saved = transactionRepository.save(transaction);

        updateAccountBalance(accountId);

        return saved;
    }

    @Transactional
    public void deleteTransaction(Long transactionId, Long operatorId) {
        VirtualAccountTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("交易记录不存在"));

        if (!transaction.getUserId().equals(operatorId)) {
            VirtualAccount account = getAccountById(transaction.getVirtualAccountId());
            if (account.getIsFamilyAccount() && account.getFamilyId() != null) {
                checkFamilyAccountPermission(account.getFamilyId(), operatorId);
            } else {
                throw new RuntimeException("无权限删除此交易记录");
            }
        }

        transactionRepository.delete(transaction);
        updateAccountBalance(transaction.getVirtualAccountId());
    }

    private void updateAccountBalance(Long accountId) {
        List<VirtualAccountTransaction> transactions = transactionRepository.findByVirtualAccountIdOrderByTransactionDateDesc(accountId);
        BigDecimal balance = BigDecimal.ZERO;
        for (VirtualAccountTransaction t : transactions) {
            if ("DEPOSIT".equals(t.getTransactionType())) {
                balance = balance.add(t.getAmount());
            } else if ("WITHDRAW".equals(t.getTransactionType())) {
                balance = balance.subtract(t.getAmount());
            }
        }
        VirtualAccount account = getAccountById(accountId);
        account.setCurrentBalance(balance);
        virtualAccountRepository.save(account);
    }

    private void checkFamilyAccountPermission(Long familyId, Long userId) {
        boolean isAdmin = familyMemberRepository.findByFamilyIdAndUserId(familyId, userId)
                .map(m -> m.getRole() == FamilyRole.ADMIN)
                .orElse(false);
        if (!isAdmin) {
            throw new RuntimeException("无权限管理家庭虚拟账户，需要管理员权限");
        }
    }
}
