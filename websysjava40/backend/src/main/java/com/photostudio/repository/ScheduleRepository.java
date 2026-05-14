package com.photostudio.repository;

import com.photostudio.entity.Employee;
import com.photostudio.entity.Schedule;
import com.photostudio.entity.Schedule.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 排班数据访问接口
 * 提供排班相关的数据库操作方法
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * 根据员工查询排班列表
     * @param employee 员工
     * @return 排班列表
     */
    List<Schedule> findByEmployee(Employee employee);

    /**
     * 根据员工和日期查询排班
     * @param employee 员工
     * @param scheduleDate 排班日期
     * @return 排班列表
     */
    List<Schedule> findByEmployeeAndScheduleDate(Employee employee, LocalDate scheduleDate);

    /**
     * 根据日期查询排班列表
     * @param scheduleDate 排班日期
     * @return 排班列表
     */
    List<Schedule> findByScheduleDate(LocalDate scheduleDate);

    /**
     * 查询指定日期范围内的排班
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 排班列表
     */
    List<Schedule> findByScheduleDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 根据状态查询排班
     * @param status 状态
     * @return 排班列表
     */
    List<Schedule> findByStatus(ScheduleStatus status);

    /**
     * 检查员工在指定时间段内是否有冲突的排班
     * @param employee 员工
     * @param scheduleDate 排班日期
     * @param start 开始时间
     * @param end 结束时间
     * @param excludeId 排除的排班ID（用于更新时排除自身）
     * @return 是否存在冲突
     */
    @Query("SELECT COUNT(s) > 0 FROM Schedule s WHERE s.employee = :employee " +
           "AND s.scheduleDate = :date AND s.status != 'CANCELLED' " +
           "AND ((s.startTime < :end AND s.endTime > :start)) " +
           "AND (:excludeId IS NULL OR s.id != :excludeId)")
    boolean existsConflict(@Param("employee") Employee employee, 
                          @Param("date") LocalDate scheduleDate,
                          @Param("start") LocalTime start, 
                          @Param("end") LocalTime end,
                          @Param("excludeId") Long excludeId);

    /**
     * 查询未提醒且日期在未来的排班
     * @param now 当前时间
     * @return 排班列表
     */
    @Query("SELECT s FROM Schedule s WHERE s.reminded = false AND s.scheduleDate >= :now")
    List<Schedule> findUnremindedSchedules(LocalDate now);
}
