package com.petclinic.service;

import com.petclinic.entity.Department;
import com.petclinic.entity.Doctor;
import com.petclinic.entity.Medicine;
import com.petclinic.entity.Schedule;
import com.petclinic.repository.DepartmentRepository;
import com.petclinic.repository.DoctorRepository;
import com.petclinic.repository.MedicineRepository;
import com.petclinic.repository.ScheduleRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 医院管理Service
 */
@Service
public class HospitalService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    // ==================== 科室管理 ====================

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    @Transactional
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Transactional
    public Department updateDepartment(Long id, Department department) {
        Optional<Department> existing = departmentRepository.findById(id);
        if (existing.isPresent()) {
            Department dept = existing.get();
            dept.setName(department.getName());
            dept.setDescription(department.getDescription());
            dept.setLocation(department.getLocation());
            dept.setStatus(department.getStatus());
            return departmentRepository.save(dept);
        }
        return null;
    }

    @Transactional
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    // ==================== 医生管理 ====================

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    @Transactional
    public Doctor createDoctor(Doctor doctor) {
        doctor.setAuditStatus(0);
        return doctorRepository.save(doctor);
    }

    @Transactional
    public Doctor updateDoctor(Long id, Doctor doctor) {
        Optional<Doctor> existing = doctorRepository.findById(id);
        if (existing.isPresent()) {
            Doctor doc = existing.get();
            doc.setName(doctor.getName());
            doc.setPhone(doctor.getPhone());
            doc.setDepartmentId(doctor.getDepartmentId());
            doc.setTitle(doctor.getTitle());
            doc.setSpecialty(doctor.getSpecialty());
            doc.setStatus(doctor.getStatus());
            return doctorRepository.save(doc);
        }
        return null;
    }

    @Transactional
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    // ==================== 排班管理 ====================

    public List<Schedule> getSchedulesByDate(LocalDate date) {
        return scheduleRepository.findByScheduleDateBetween(date, date);
    }

    public List<Schedule> getSchedulesByDateRange(LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findByScheduleDateBetween(startDate, endDate);
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    @Transactional
    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public List<Schedule> batchCreateSchedule(List<Schedule> schedules) {
        return scheduleRepository.saveAll(schedules);
    }

    /**
     * 批量导入排班（Excel）
     */
    @Transactional
    public String importScheduleFromExcel(MultipartFile file) throws IOException {
        List<Schedule> schedules = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        int successCount = 0;
        int errorCount = 0;
        StringBuilder errorMsg = new StringBuilder();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            try {
                Schedule schedule = new Schedule();
                schedule.setDoctorId((long) row.getCell(0).getNumericCellValue());
                schedule.setDepartmentId((long) row.getCell(1).getNumericCellValue());
                schedule.setScheduleDate(row.getCell(2).getLocalDateTimeCellValue().toLocalDate());
                schedule.setStartTime(LocalTime.parse(row.getCell(3).getStringCellValue()));
                schedule.setEndTime(LocalTime.parse(row.getCell(4).getStringCellValue()));
                schedule.setMaxCount((int) row.getCell(5).getNumericCellValue());
                schedule.setBookedCount(0);
                schedule.setStatus(1);
                schedules.add(schedule);
                successCount++;
            } catch (Exception e) {
                errorCount++;
                errorMsg.append("第").append(i + 1).append("行: ").append(e.getMessage()).append("; ");
            }
        }

        scheduleRepository.saveAll(schedules);
        workbook.close();

        return String.format("成功导入%d条，失败%d条。%s", successCount, errorCount, errorMsg.toString());
    }

    @Transactional
    public Schedule updateSchedule(Long id, Schedule schedule) {
        Optional<Schedule> existing = scheduleRepository.findById(id);
        if (existing.isPresent()) {
            Schedule sch = existing.get();
            sch.setDoctorId(schedule.getDoctorId());
            sch.setDepartmentId(schedule.getDepartmentId());
            sch.setScheduleDate(schedule.getScheduleDate());
            sch.setStartTime(schedule.getStartTime());
            sch.setEndTime(schedule.getEndTime());
            sch.setMaxCount(schedule.getMaxCount());
            sch.setStatus(schedule.getStatus());
            sch.setRemark(schedule.getRemark());
            return scheduleRepository.save(sch);
        }
        return null;
    }

    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    // ==================== 药品管理 ====================

    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    public List<Medicine> getWarningMedicines() {
        return medicineRepository.findWarningMedicines();
    }

    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id).orElse(null);
    }

    @Transactional
    public Medicine createMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    @Transactional
    public Medicine updateMedicine(Long id, Medicine medicine) {
        Optional<Medicine> existing = medicineRepository.findById(id);
        if (existing.isPresent()) {
            Medicine med = existing.get();
            med.setName(medicine.getName());
            med.setCategory(medicine.getCategory());
            med.setSpecification(medicine.getSpecification());
            med.setManufacturer(medicine.getManufacturer());
            med.setStockQuantity(medicine.getStockQuantity());
            med.setWarningQuantity(medicine.getWarningQuantity());
            med.setPrice(medicine.getPrice());
            med.setUnit(medicine.getUnit());
            med.setDescription(medicine.getDescription());
            med.setStatus(medicine.getStatus());
            return medicineRepository.save(med);
        }
        return null;
    }

    @Transactional
    public void deleteMedicine(Long id) {
        medicineRepository.deleteById(id);
    }
}
