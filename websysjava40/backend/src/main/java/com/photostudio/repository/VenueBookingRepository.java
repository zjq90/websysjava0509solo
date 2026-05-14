package com.photostudio.repository;

import com.photostudio.entity.Venue;
import com.photostudio.entity.VenueBooking;
import com.photostudio.entity.VenueBooking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 场地预约数据访问接口
 * 提供场地预约相关的数据库操作方法
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface VenueBookingRepository extends JpaRepository<VenueBooking, Long> {

    /**
     * 根据场地查询预约列表
     * @param venue 场地
     * @return 预约列表
     */
    List<VenueBooking> findByVenue(Venue venue);

    /**
     * 根据场地和日期查询预约
     * @param venue 场地
     * @param bookingDate 预约日期
     * @return 预约列表
     */
    List<VenueBooking> findByVenueAndBookingDate(Venue venue, LocalDate bookingDate);

    /**
     * 根据日期查询预约列表
     * @param bookingDate 预约日期
     * @return 预约列表
     */
    List<VenueBooking> findByBookingDate(LocalDate bookingDate);

    /**
     * 根据状态查询预约
     * @param status 状态
     * @return 预约列表
     */
    List<VenueBooking> findByStatus(BookingStatus status);

    /**
     * 检查场地在指定时间段内是否有冲突的预约
     * @param venue 场地
     * @param bookingDate 预约日期
     * @param start 开始时间
     * @param end 结束时间
     * @param excludeId 排除的预约ID（用于更新时排除自身）
     * @return 是否存在冲突
     */
    @Query("SELECT COUNT(vb) > 0 FROM VenueBooking vb WHERE vb.venue = :venue " +
           "AND vb.bookingDate = :date AND vb.status != 'CANCELLED' " +
           "AND ((vb.startTime < :end AND vb.endTime > :start)) " +
           "AND (:excludeId IS NULL OR vb.id != :excludeId)")
    boolean existsConflict(@Param("venue") Venue venue, 
                          @Param("date") LocalDate bookingDate,
                          @Param("start") LocalTime start, 
                          @Param("end") LocalTime end,
                          @Param("excludeId") Long excludeId);
}
