package com.hospital.finance.config;

import com.hospital.finance.entity.*;
import com.hospital.finance.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 数据初始化类
 * 系统启动时自动生成测试数据
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private OutpatientChargeRepository outpatientChargeRepository;

    @Autowired
    private OutpatientChargeDetailRepository outpatientChargeDetailRepository;

    @Autowired
    private InpatientChargeRepository inpatientChargeRepository;

    @Autowired
    private InpatientChargeDetailRepository inpatientChargeDetailRepository;

    @Autowired
    private InsuranceSettlementRepository insuranceSettlementRepository;

    @Override
    public void run(String... args) throws Exception {
        initPatients();
        initOutpatientCharges();
        initInpatientCharges();
        initInsuranceSettlements();
    }

    private void initPatients() {
        String[][] patientData = {
                {"P001", "张三", "男", "35", "110101198901011234", "13800138001", "北京市朝阳区", "true", "INS001", "城镇职工"},
                {"P002", "李四", "女", "28", "110101199602022345", "13800138002", "北京市海淀区", "true", "INS002", "城乡居民"},
                {"P003", "王五", "男", "45", "110101197903033456", "13800138003", "北京市西城区", "false", null, null},
                {"P004", "赵六", "女", "52", "110101197204044567", "13800138004", "北京市东城区", "true", "INS003", "城镇职工"},
                {"P005", "钱七", "男", "60", "110101196405055678", "13800138005", "北京市丰台区", "true", "INS004", "新农合"}
        };

        for (String[] data : patientData) {
            Patient patient = new Patient();
            patient.setPatientNo(data[0]);
            patient.setName(data[1]);
            patient.setGender(data[2]);
            patient.setAge(Integer.valueOf(data[3]));
            patient.setIdCard(data[4]);
            patient.setPhone(data[5]);
            patient.setAddress(data[6]);
            patient.setHasInsurance(Boolean.valueOf(data[7]));
            patient.setInsuranceNo(data[8]);
            patient.setInsuranceType(data[9]);
            patientRepository.save(patient);
        }
    }

    private void initOutpatientCharges() {
        String[][] chargeData = {
                {"OP20240101001", "1", "张三", "内科", "王医生", "256.50", "179.55", "76.95", "已缴费", "微信"},
                {"OP20240101002", "2", "李四", "外科", "李医生", "580.00", "406.00", "174.00", "已缴费", "支付宝"},
                {"OP20240101003", "3", "王五", "儿科", "张医生", "120.00", "0.00", "120.00", "已缴费", "现金"},
                {"OP20240101004", "4", "赵六", "内科", "王医生", "890.50", "623.35", "267.15", "待缴费", null}
        };

        for (String[] data : chargeData) {
            OutpatientCharge charge = new OutpatientCharge();
            charge.setChargeNo(data[0]);
            charge.setPatientId(Long.valueOf(data[1]));
            charge.setPatientName(data[2]);
            charge.setDepartment(data[3]);
            charge.setDoctorName(data[4]);
            charge.setTotalAmount(new BigDecimal(data[5]));
            charge.setInsuranceAmount(new BigDecimal(data[6]));
            charge.setSelfPayAmount(new BigDecimal(data[7]));
            charge.setStatus(data[8]);
            charge.setPaymentMethod(data[9]);
            charge.setOperator("收费员A");
            charge.setChargeTime(LocalDateTime.now());
            outpatientChargeRepository.save(charge);
        }

        String[][] detailData = {
                {"1", "OP20240101001", "药品", "MED001", "阿莫西林胶囊", "0.25g*24粒", "盒", "2", "28.50", "57.00", "true", "0.7"},
                {"1", "OP20240101001", "检查", "EX001", "血常规", "", "次", "1", "45.00", "45.00", "true", "0.7"},
                {"1", "OP20240101001", "治疗", "TR001", "肌肉注射", "", "次", "2", "12.50", "25.00", "true", "0.7"},
                {"2", "OP20240101002", "药品", "MED002", "头孢克肟分散片", "0.1g*12片", "盒", "3", "42.00", "126.00", "true", "0.7"},
                {"2", "OP20240101002", "检查", "EX002", "CT检查", "", "次", "1", "450.00", "450.00", "true", "0.7"}
        };

        for (String[] data : detailData) {
            OutpatientChargeDetail detail = new OutpatientChargeDetail();
            detail.setChargeId(Long.valueOf(data[0]));
            detail.setChargeNo(data[1]);
            detail.setItemType(data[2]);
            detail.setItemCode(data[3]);
            detail.setItemName(data[4]);
            detail.setSpecification(data[5]);
            detail.setUnit(data[6]);
            detail.setQuantity(Integer.valueOf(data[7]));
            detail.setUnitPrice(new BigDecimal(data[8]));
            detail.setAmount(new BigDecimal(data[9]));
            detail.setIsInsurance(Boolean.valueOf(data[10]));
            detail.setInsuranceRatio(Double.valueOf(data[11]));
            BigDecimal amount = new BigDecimal(data[9]);
            BigDecimal insuranceAmount = amount.multiply(BigDecimal.valueOf(Double.parseDouble(data[11])));
            detail.setInsuranceAmount(insuranceAmount);
            detail.setSelfPayAmount(amount.subtract(insuranceAmount));
            outpatientChargeDetailRepository.save(detail);
        }
    }

    private void initInpatientCharges() {
        String[][] chargeData = {
                {"IP20240101001", "1", "张三", "ZY001", "内科", "302", "王医生", "5000.00", "2000.00", "3500.00", "1500.00", "已结算", "银行卡"},
                {"IP20240101002", "2", "李四", "ZY002", "外科", "205", "李医生", "8500.00", "3000.00", "5950.00", "2550.00", "住院中", null}
        };

        for (String[] data : chargeData) {
            InpatientCharge charge = new InpatientCharge();
            charge.setChargeNo(data[0]);
            charge.setPatientId(Long.valueOf(data[1]));
            charge.setPatientName(data[2]);
            charge.setAdmissionNo(data[3]);
            charge.setDepartment(data[4]);
            charge.setBedNo(data[5]);
            charge.setDoctorName(data[6]);
            charge.setTotalAmount(new BigDecimal(data[7]));
            charge.setDepositAmount(new BigDecimal(data[8]));
            charge.setInsuranceAmount(new BigDecimal(data[9]));
            charge.setSelfPayAmount(new BigDecimal(data[10]));
            charge.setStatus(data[11]);
            charge.setPaymentMethod(data[12]);
            charge.setOperator("收费员B");
            charge.setChargeTime(LocalDateTime.now());
            inpatientChargeRepository.save(charge);
        }

        String[][] detailData = {
                {"1", "IP20240101001", "床位", "BD001", "普通病床", "", "天", "5", "80.00", "400.00", "true", "0.8"},
                {"1", "IP20240101001", "护理", "NS001", "一级护理", "", "天", "5", "50.00", "250.00", "true", "0.8"},
                {"1", "IP20240101001", "药品", "MED003", "注射用头孢曲松钠", "1g", "支", "10", "45.00", "450.00", "true", "0.8"},
                {"1", "IP20240101001", "检查", "EX003", "B超检查", "", "次", "1", "120.00", "120.00", "true", "0.8"},
                {"1", "IP20240101001", "治疗", "TR002", "静脉输液", "", "次", "5", "30.00", "150.00", "true", "0.8"}
        };

        for (String[] data : detailData) {
            InpatientChargeDetail detail = new InpatientChargeDetail();
            detail.setChargeId(Long.valueOf(data[0]));
            detail.setChargeNo(data[1]);
            detail.setItemType(data[2]);
            detail.setItemCode(data[3]);
            detail.setItemName(data[4]);
            detail.setSpecification(data[5]);
            detail.setUnit(data[6]);
            detail.setQuantity(Integer.valueOf(data[7]));
            detail.setUnitPrice(new BigDecimal(data[8]));
            detail.setAmount(new BigDecimal(data[9]));
            detail.setIsInsurance(Boolean.valueOf(data[10]));
            detail.setInsuranceRatio(Double.valueOf(data[11]));
            BigDecimal amount = new BigDecimal(data[9]);
            BigDecimal insuranceAmount = amount.multiply(BigDecimal.valueOf(Double.parseDouble(data[11])));
            detail.setInsuranceAmount(insuranceAmount);
            detail.setSelfPayAmount(amount.subtract(insuranceAmount));
            detail.setChargeDate(LocalDateTime.now());
            inpatientChargeDetailRepository.save(detail);
        }
    }

    private void initInsuranceSettlements() {
        String[][] settlementData = {
                {"SET001", "1", "张三", "INS001", "城镇职工", "门诊", "1", "OP20240101001", "256.50", "179.55", "76.95", "179.55", "76.95", "已结算"},
                {"SET002", "1", "张三", "INS001", "城镇职工", "住院", "1", "IP20240101001", "5000.00", "4000.00", "1000.00", "3500.00", "1500.00", "已结算"}
        };

        for (String[] data : settlementData) {
            InsuranceSettlement settlement = new InsuranceSettlement();
            settlement.setSettlementNo(data[0]);
            settlement.setPatientId(Long.valueOf(data[1]));
            settlement.setPatientName(data[2]);
            settlement.setInsuranceNo(data[3]);
            settlement.setInsuranceType(data[4]);
            settlement.setChargeType(data[5]);
            settlement.setChargeId(Long.valueOf(data[6]));
            settlement.setChargeNo(data[7]);
            settlement.setTotalAmount(new BigDecimal(data[8]));
            settlement.setWithinScopeAmount(new BigDecimal(data[9]));
            settlement.setOutsideScopeAmount(new BigDecimal(data[10]));
            settlement.setInsurancePayAmount(new BigDecimal(data[11]));
            settlement.setSelfPayAmount(new BigDecimal(data[12]));
            settlement.setStatus(data[13]);
            settlement.setSettlementTime(LocalDateTime.now());
            settlement.setOperator("医保结算员");
            settlement.setInsuranceResponse("{\"code\":\"0000\",\"message\":\"结算成功\"}");
            insuranceSettlementRepository.save(settlement);
        }
    }
}