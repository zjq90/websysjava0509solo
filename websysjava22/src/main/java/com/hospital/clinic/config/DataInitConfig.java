package com.hospital.clinic.config;

import com.hospital.clinic.entity.*;
import com.hospital.clinic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 数据初始化类
 * 项目启动时自动生成测试数据
 */
@Component
public class DataInitConfig implements CommandLineRunner {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DrugRepository drugRepository;

    @Override
    public void run(String... args) throws Exception {
        initDepartment();
        initDoctor();
        initPatient();
        initSchedule();
        initAppointment();
        initDrug();
    }

    /**
     * 初始化科室数据
     */
    private void initDepartment() {
        if (departmentRepository.count() > 0) {
            return;
        }

        String[][] depts = {
                {"D001", "内科", "临床科室", "门诊楼1层", "010-12345678"},
                {"D002", "外科", "临床科室", "门诊楼2层", "010-12345679"},
                {"D003", "儿科", "临床科室", "门诊楼3层", "010-12345680"},
                {"D004", "妇产科", "临床科室", "门诊楼4层", "010-12345681"},
                {"D005", "眼科", "临床科室", "门诊楼5层", "010-12345682"},
                {"D006", "口腔科", "临床科室", "门诊楼6层", "010-12345683"}
        };

        for (String[] dept : depts) {
            Department department = new Department();
            department.setDeptCode(dept[0]);
            department.setDeptName(dept[1]);
            department.setDeptType(dept[2]);
            department.setLocation(dept[3]);
            department.setPhone(dept[4]);
            department.setStatus(1);
            departmentRepository.save(department);
        }
    }

    /**
     * 初始化医生数据
     */
    private void initDoctor() {
        if (doctorRepository.count() > 0) {
            return;
        }

        String[][] doctors = {
                {"DOC001", "张医生", "男", "主任医师", "擅长心血管疾病", "1"},
                {"DOC002", "李医生", "女", "副主任医师", "擅长消化系统疾病", "1"},
                {"DOC003", "王医生", "男", "主治医师", "擅长呼吸系统疾病", "1"},
                {"DOC004", "赵医生", "女", "主任医师", "擅长骨科疾病", "2"},
                {"DOC005", "刘医生", "男", "副主任医师", "擅长普通外科", "2"},
                {"DOC006", "陈医生", "女", "主治医师", "擅长小儿内科", "3"}
        };

        for (String[] doc : doctors) {
            Doctor doctor = new Doctor();
            doctor.setDoctorNo(doc[0]);
            doctor.setName(doc[1]);
            doctor.setGender(doc[2]);
            doctor.setTitle(doc[3]);
            doctor.setSpecialty(doc[4]);
            doctor.setDepartment(departmentRepository.findById(Long.parseLong(doc[5])).orElse(null));
            doctor.setConsultationFee(new BigDecimal("50.00"));
            doctor.setPhone("1380013800" + doc[0].charAt(5));
            doctor.setStatus(1);
            doctorRepository.save(doctor);
        }
    }

    /**
     * 初始化患者数据
     */
    private void initPatient() {
        if (patientRepository.count() > 0) {
            return;
        }

        String[][] patients = {
                {"P001", "张三", "男", "30", "110101199301011234", "13900139001"},
                {"P002", "李四", "女", "25", "110101199802021234", "13900139002"},
                {"P003", "王五", "男", "45", "110101197803031234", "13900139003"},
                {"P004", "赵六", "女", "35", "110101198804041234", "13900139004"},
                {"P005", "钱七", "男", "50", "110101197305051234", "13900139005"},
                {"P006", "孙八", "女", "28", "110101199506061234", "13900139006"}
        };

        for (String[] pat : patients) {
            Patient patient = new Patient();
            patient.setPatientNo(pat[0]);
            patient.setName(pat[1]);
            patient.setGender(pat[2]);
            patient.setAge(Integer.parseInt(pat[3]));
            patient.setIdCard(pat[4]);
            patient.setPhone(pat[5]);
            patient.setAddress("北京市朝阳区");
            patient.setEmergencyContact("家属");
            patient.setEmergencyPhone("13800000000");
            patient.setStatus(1);
            patientRepository.save(patient);
        }
    }

    /**
     * 初始化排班数据
     */
    private void initSchedule() {
        if (scheduleRepository.count() > 0) {
            return;
        }

        LocalDate today = LocalDate.now();
        Doctor doctor1 = doctorRepository.findById(1L).orElse(null);
        Doctor doctor2 = doctorRepository.findById(2L).orElse(null);

        if (doctor1 != null && doctor2 != null) {
            Schedule schedule1 = new Schedule();
            schedule1.setDoctor(doctor1);
            schedule1.setDepartment(doctor1.getDepartment());
            schedule1.setScheduleDate(today);
            schedule1.setTimeSlot("上午");
            schedule1.setStartTime(LocalTime.of(8, 0));
            schedule1.setEndTime(LocalTime.of(12, 0));
            schedule1.setTotalSlots(20);
            schedule1.setRemainingSlots(15);
            schedule1.setRoomNo("101");
            schedule1.setStatus(1);
            scheduleRepository.save(schedule1);

            Schedule schedule2 = new Schedule();
            schedule2.setDoctor(doctor2);
            schedule2.setDepartment(doctor2.getDepartment());
            schedule2.setScheduleDate(today);
            schedule2.setTimeSlot("下午");
            schedule2.setStartTime(LocalTime.of(14, 0));
            schedule2.setEndTime(LocalTime.of(18, 0));
            schedule2.setTotalSlots(20);
            schedule2.setRemainingSlots(18);
            schedule2.setRoomNo("102");
            schedule2.setStatus(1);
            scheduleRepository.save(schedule2);
        }
    }

    /**
     * 初始化预约数据
     */
    private void initAppointment() {
        if (appointmentRepository.count() > 0) {
            return;
        }

        LocalDate today = LocalDate.now();
        Patient patient1 = patientRepository.findById(1L).orElse(null);
        Patient patient2 = patientRepository.findById(2L).orElse(null);
        Patient patient3 = patientRepository.findById(3L).orElse(null);
        Doctor doctor1 = doctorRepository.findById(1L).orElse(null);

        if (patient1 != null && patient2 != null && patient3 != null && doctor1 != null) {
            String[] statuses = {"已预约", "已叫号", "已完成"};
            int[] minutes = {0, 30, 0};  // 确保分钟在0-59范围内
            int[] hours = {9, 9, 10};   // 第三个预约在10:00
            for (int i = 0; i < 3; i++) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentNo("APT" + System.currentTimeMillis() + i);
                appointment.setPatient(i == 0 ? patient1 : (i == 1 ? patient2 : patient3));
                appointment.setDoctor(doctor1);
                appointment.setDepartment(doctor1.getDepartment());
                appointment.setAppointmentDate(today);
                appointment.setAppointmentTime(LocalTime.of(hours[i], minutes[i]));
                appointment.setQueueNumber(i + 1);
                appointment.setStatus(statuses[i]);
                appointment.setConsultationFee(new BigDecimal("50.00"));
                appointmentRepository.save(appointment);
            }
        }
    }

    /**
     * 初始化药品数据
     */
    private void initDrug() {
        if (drugRepository.count() > 0) {
            return;
        }

        String[][] drugs = {
                {"DRUG001", "阿莫西林胶囊", "抗生素", "0.25g*24粒", "华北制药", "15.00", "100", "10"},
                {"DRUG002", "布洛芬缓释胶囊", "解热镇痛", "0.3g*20粒", "中美史克", "25.00", "80", "10"},
                {"DRUG003", "维生素C片", "维生素", "100mg*100片", "东北制药", "8.00", "200", "20"},
                {"DRUG004", "感冒灵颗粒", "感冒药", "10g*9袋", "三九医药", "12.00", "150", "15"},
                {"DRUG005", "头孢拉定胶囊", "抗生素", "0.25g*24粒", "扬子江药业", "18.00", "5", "10"}
        };

        for (String[] drug : drugs) {
            Drug d = new Drug();
            d.setDrugCode(drug[0]);
            d.setDrugName(drug[1]);
            d.setDrugType(drug[2]);
            d.setDrugSpec(drug[3]);
            d.setManufacturer(drug[4]);
            d.setPrice(new BigDecimal(drug[5]));
            d.setStockQuantity(Integer.parseInt(drug[6]));
            d.setMinStock(Integer.parseInt(drug[7]));
            d.setStatus(1);
            drugRepository.save(d);
        }
    }
}
