package com.personal.accounting.config;

import com.personal.accounting.entity.*;
import com.personal.accounting.enums.*;
import com.personal.accounting.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final DebtRepository debtRepository;
    private final AssetRepository assetRepository;
    private final InvestmentRepository investmentRepository;
    private final BudgetRepository budgetRepository;
    private final VirtualAccountRepository virtualAccountRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            initUsers();
        }
    }

    private void initUsers() {
        User user1 = new User();
        user1.setUsername("admin");
        user1.setPassword("123456");
        user1.setNickname("管理员");
        user1.setEmail("admin@example.com");
        user1.setPhone("13800138000");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("member1");
        user2.setPassword("123456");
        user2.setNickname("家庭成员1");
        user2.setEmail("member1@example.com");
        user2.setPhone("13800138001");
        userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("member2");
        user3.setPassword("123456");
        user3.setNickname("家庭成员2");
        user3.setEmail("member2@example.com");
        user3.setPhone("13800138002");
        userRepository.save(user3);

        Family family = new Family();
        family.setFamilyName("快乐一家人");
        family.setDescription("家庭共享账户");
        family.setCreatedBy(user1.getId());
        familyRepository.save(family);

        FamilyMember member1 = new FamilyMember();
        member1.setFamilyId(family.getId());
        member1.setUserId(user1.getId());
        member1.setRole(FamilyRole.ADMIN);
        familyMemberRepository.save(member1);

        FamilyMember member2 = new FamilyMember();
        member2.setFamilyId(family.getId());
        member2.setUserId(user2.getId());
        member2.setRole(FamilyRole.MEMBER);
        familyMemberRepository.save(member2);

        initDebts(user1.getId(), user2.getId());
        initAssets(user1.getId(), user2.getId());
        initInvestments(user1.getId());
        initBudgets(family.getId(), user1.getId());
        initVirtualAccounts(user1.getId(), family.getId());
    }

    private void initDebts(Long userId1, Long userId2) {
        Debt debt1 = new Debt();
        debt1.setUserId(userId1);
        debt1.setDebtType(DebtType.CREDIT_CARD);
        debt1.setDebtName("招商银行信用卡");
        debt1.setCreditor("招商银行");
        debt1.setPrincipalAmount(new BigDecimal("50000"));
        debt1.setRemainingAmount(new BigDecimal("35000"));
        debt1.setAnnualInterestRate(new BigDecimal("18.25"));
        debt1.setRepaymentMethod(RepaymentMethod.EQUAL_PRINCIPAL_INTEREST);
        debt1.setLoanTermMonths(12);
        debt1.setStartDate(LocalDate.now().minusMonths(3));
        debt1.setNextPaymentDate(LocalDate.now().plusDays(2));
        debt1.setPaymentDay(10);
        debt1.setTotalInterestPaid(new BigDecimal("856.32"));
        debt1.setMonthlyPayment(new BigDecimal("3256.80"));
        debtRepository.save(debt1);

        Debt debt2 = new Debt();
        debt2.setUserId(userId1);
        debt2.setDebtType(DebtType.MORTGAGE);
        debt2.setDebtName("住房贷款");
        debt2.setCreditor("工商银行");
        debt2.setPrincipalAmount(new BigDecimal("1000000"));
        debt2.setRemainingAmount(new BigDecimal("850000"));
        debt2.setAnnualInterestRate(new BigDecimal("4.2"));
        debt2.setRepaymentMethod(RepaymentMethod.EQUAL_PRINCIPAL_INTEREST);
        debt2.setLoanTermMonths(360);
        debt2.setStartDate(LocalDate.now().minusYears(2));
        debt2.setNextPaymentDate(LocalDate.now().plusDays(15));
        debt2.setPaymentDay(20);
        debt2.setTotalInterestPaid(new BigDecimal("75600"));
        debt2.setMonthlyPayment(new BigDecimal("4890.17"));
        debtRepository.save(debt2);

        Debt debt3 = new Debt();
        debt3.setUserId(userId2);
        debt3.setDebtType(DebtType.CAR_LOAN);
        debt3.setDebtName("车贷");
        debt3.setCreditor("建设银行");
        debt3.setPrincipalAmount(new BigDecimal("150000"));
        debt3.setRemainingAmount(new BigDecimal("75000"));
        debt3.setAnnualInterestRate(new BigDecimal("5.6"));
        debt3.setRepaymentMethod(RepaymentMethod.EQUAL_PRINCIPAL);
        debt3.setLoanTermMonths(36);
        debt3.setStartDate(LocalDate.now().minusMonths(18));
        debt3.setNextPaymentDate(LocalDate.now().plusDays(5));
        debt3.setPaymentDay(15);
        debt3.setTotalInterestPaid(new BigDecimal("4200"));
        debt3.setMonthlyPayment(new BigDecimal("4512.50"));
        debtRepository.save(debt3);
    }

    private void initAssets(Long userId1, Long userId2) {
        Asset asset1 = new Asset();
        asset1.setUserId(userId1);
        asset1.setAssetType(AssetType.CASH);
        asset1.setAssetName("现金");
        asset1.setCurrentValue(new BigDecimal("5000"));
        asset1.setOriginalValue(new BigDecimal("5000"));
        assetRepository.save(asset1);

        Asset asset2 = new Asset();
        asset2.setUserId(userId1);
        asset2.setAssetType(AssetType.BANK_DEPOSIT);
        asset2.setAssetName("定期存款");
        asset2.setCurrentValue(new BigDecimal("200000"));
        asset2.setOriginalValue(new BigDecimal("200000"));
        assetRepository.save(asset2);

        Asset asset3 = new Asset();
        asset3.setUserId(userId1);
        asset3.setAssetType(AssetType.REAL_ESTATE);
        asset3.setAssetName("自住房产");
        asset3.setCurrentValue(new BigDecimal("2500000"));
        asset3.setOriginalValue(new BigDecimal("1800000"));
        asset3.setLocation("北京市朝阳区");
        assetRepository.save(asset3);

        Asset asset4 = new Asset();
        asset4.setUserId(userId2);
        asset4.setAssetType(AssetType.CASH);
        asset4.setAssetName("现金");
        asset4.setCurrentValue(new BigDecimal("3000"));
        asset4.setOriginalValue(new BigDecimal("3000"));
        assetRepository.save(asset4);

        Asset asset5 = new Asset();
        asset5.setUserId(userId2);
        asset5.setAssetType(AssetType.VEHICLE);
        asset5.setAssetName("家用汽车");
        asset5.setCurrentValue(new BigDecimal("120000"));
        asset5.setOriginalValue(new BigDecimal("180000"));
        assetRepository.save(asset5);
    }

    private void initInvestments(Long userId) {
        Investment inv1 = new Investment();
        inv1.setUserId(userId);
        inv1.setInvestmentType(InvestmentType.STOCK);
        inv1.setInvestmentName("贵州茅台");
        inv1.setCode("600519");
        inv1.setQuantity(new BigDecimal("100"));
        inv1.setCostPrice(new BigDecimal("1680.00"));
        inv1.setCurrentPrice(new BigDecimal("1750.00"));
        inv1.setPurchaseDate(LocalDate.now().minusMonths(6).atStartOfDay());
        investmentRepository.save(inv1);

        Investment inv2 = new Investment();
        inv2.setUserId(userId);
        inv2.setInvestmentType(InvestmentType.FUND);
        inv2.setInvestmentName("易方达蓝筹精选");
        inv2.setCode("005827");
        inv2.setQuantity(new BigDecimal("50000"));
        inv2.setCostPrice(new BigDecimal("2.15"));
        inv2.setCurrentPrice(new BigDecimal("2.08"));
        inv2.setPurchaseDate(LocalDate.now().minusMonths(8).atStartOfDay());
        investmentRepository.save(inv2);

        Investment inv3 = new Investment();
        inv3.setUserId(userId);
        inv3.setInvestmentType(InvestmentType.CRYPTOCURRENCY);
        inv3.setInvestmentName("比特币");
        inv3.setCode("BTC");
        inv3.setQuantity(new BigDecimal("0.5"));
        inv3.setCostPrice(new BigDecimal("280000.00"));
        inv3.setCurrentPrice(new BigDecimal("320000.00"));
        inv3.setPurchaseDate(LocalDate.now().minusMonths(12).atStartOfDay());
        investmentRepository.save(inv3);
    }

    private void initBudgets(Long familyId, Long userId) {
        Budget budget1 = new Budget();
        budget1.setFamilyId(familyId);
        budget1.setUserId(userId);
        budget1.setBudgetName("餐饮预算");
        budget1.setCategory(BudgetCategory.FOOD);
        budget1.setTotalAmount(new BigDecimal("3000"));
        budget1.setSpentAmount(new BigDecimal("1850"));
        budget1.setBudgetMonth(YearMonth.now());
        budget1.setDescription("家庭月度餐饮预算");
        budgetRepository.save(budget1);

        Budget budget2 = new Budget();
        budget2.setFamilyId(familyId);
        budget2.setUserId(userId);
        budget2.setBudgetName("交通预算");
        budget2.setCategory(BudgetCategory.TRANSPORTATION);
        budget2.setTotalAmount(new BigDecimal("1000"));
        budget2.setSpentAmount(new BigDecimal("680"));
        budget2.setBudgetMonth(YearMonth.now());
        budget2.setDescription("交通出行预算");
        budgetRepository.save(budget2);

        Budget budget3 = new Budget();
        budget3.setFamilyId(familyId);
        budget3.setUserId(userId);
        budget3.setBudgetName("娱乐预算");
        budget3.setCategory(BudgetCategory.ENTERTAINMENT);
        budget3.setTotalAmount(new BigDecimal("800"));
        budget3.setSpentAmount(new BigDecimal("420"));
        budget3.setBudgetMonth(YearMonth.now());
        budget3.setDescription("休闲娱乐预算");
        budgetRepository.save(budget3);
    }

    private void initVirtualAccounts(Long userId, Long familyId) {
        VirtualAccount va1 = new VirtualAccount();
        va1.setUserId(userId);
        va1.setAccountName("旅行基金");
        va1.setCurrentBalance(new BigDecimal("8500"));
        va1.setTargetAmount(new BigDecimal("20000"));
        va1.setTargetDate(LocalDate.now().plusMonths(8));
        va1.setPurpose("年度旅行专项储蓄");
        va1.setColor("#FF6B6B");
        va1.setIcon("✈️");
        va1.setIsFamilyAccount(true);
        va1.setFamilyId(familyId);
        virtualAccountRepository.save(va1);

        VirtualAccount va2 = new VirtualAccount();
        va2.setUserId(userId);
        va2.setAccountName("教育基金");
        va2.setCurrentBalance(new BigDecimal("15000"));
        va2.setTargetAmount(new BigDecimal("50000"));
        va2.setTargetDate(LocalDate.now().plusYears(2));
        va2.setPurpose("子女教育储蓄");
        va2.setColor("#4ECDC4");
        va2.setIcon("📚");
        va2.setIsFamilyAccount(true);
        va2.setFamilyId(familyId);
        virtualAccountRepository.save(va2);

        VirtualAccount va3 = new VirtualAccount();
        va3.setUserId(userId);
        va3.setAccountName("应急备用金");
        va3.setCurrentBalance(new BigDecimal("10000"));
        va3.setTargetAmount(new BigDecimal("30000"));
        va3.setPurpose("紧急备用资金");
        va3.setColor("#FFE66D");
        va3.setIcon("🏦");
        va3.setIsFamilyAccount(false);
        virtualAccountRepository.save(va3);
    }
}
