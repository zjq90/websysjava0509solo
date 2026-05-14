package com.photostudio.service;

import com.photostudio.entity.Venue;
import com.photostudio.entity.VenueBooking;
import com.photostudio.entity.VenueBooking.BookingStatus;
import com.photostudio.repository.VenueBookingRepository;
import com.photostudio.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * 场地预约服务类
 * 提供场地预约管理相关的业务逻辑
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
public class VenueBookingService {

    private final VenueBookingRepository venueBookingRepository;
    private final VenueRepository venueRepository;

    @Autowired
    public VenueBookingService(VenueBookingRepository venueBookingRepository, VenueRepository venueRepository) {
        this.venueBookingRepository = venueBookingRepository;
        this.venueRepository = venueRepository;
    }

    /**
     * 获取所有场地预约
     * @return 预约列表
     */
    public List<VenueBooking> getAllBookings() {
        return venueBookingRepository.findAll();
    }

    /**
     * 根据ID获取预约
     * @param id 预约ID
     * @return 预约信息
     */
    public Optional<VenueBooking> getBookingById(Long id) {
        return venueBookingRepository.findById(id);
    }

    /**
     * 根据场地获取预约
     * @param venueId 场地ID
     * @return 预约列表
     */
    public List<VenueBooking> getBookingsByVenue(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("场地不存在"));
        return venueBookingRepository.findByVenue(venue);
    }

    /**
     * 根据日期获取预约
     * @param date 日期
     * @return 预约列表
     */
    public List<VenueBooking> getBookingsByDate(LocalDate date) {
        return venueBookingRepository.findByBookingDate(date);
    }

    /**
     * 根据状态获取预约
     * @param status 状态
     * @return 预约列表
     */
    public List<VenueBooking> getBookingsByStatus(BookingStatus status) {
        return venueBookingRepository.findByStatus(status);
    }

    /**
     * 获取场地在指定日期的预约
     * @param venueId 场地ID
     * @param date 日期
     * @return 预约列表
     */
    public List<VenueBooking> getVenueBookingsByDate(Long venueId, LocalDate date) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("场地不存在"));
        return venueBookingRepository.findByVenueAndBookingDate(venue, date);
    }

    /**
     * 创建场地预约
     * @param booking 预约信息
     * @return 创建的预约
     */
    @Transactional
    public VenueBooking createBooking(VenueBooking booking) {
        Venue venue = venueRepository.findById(booking.getVenue().getId())
                .orElseThrow(() -> new RuntimeException("场地不存在"));
        
        if (!venue.getAvailable()) {
            throw new RuntimeException("场地不可用");
        }
        
        if (venueBookingRepository.existsConflict(venue, booking.getBookingDate(), 
                booking.getStartTime(), booking.getEndTime(), null)) {
            throw new RuntimeException("该场地在指定时间已有预约");
        }
        
        booking.setVenue(venue);
        return venueBookingRepository.save(booking);
    }

    /**
     * 更新场地预约
     * @param id 预约ID
     * @param booking 预约信息
     * @return 更新后的预约
     */
    @Transactional
    public VenueBooking updateBooking(Long id, VenueBooking booking) {
        VenueBooking existing = venueBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("预约不存在"));
        
        Venue venue = venueRepository.findById(booking.getVenue().getId())
                .orElseThrow(() -> new RuntimeException("场地不存在"));
        
        if (venueBookingRepository.existsConflict(venue, booking.getBookingDate(), 
                booking.getStartTime(), booking.getEndTime(), id)) {
            throw new RuntimeException("该场地在指定时间已有预约");
        }
        
        existing.setVenue(venue);
        existing.setBookingDate(booking.getBookingDate());
        existing.setStartTime(booking.getStartTime());
        existing.setEndTime(booking.getEndTime());
        existing.setCustomerName(booking.getCustomerName());
        existing.setCustomerPhone(booking.getCustomerPhone());
        existing.setShootingTheme(booking.getShootingTheme());
        existing.setStatus(booking.getStatus());
        existing.setRemark(booking.getRemark());
        
        return venueBookingRepository.save(existing);
    }

    /**
     * 删除场地预约
     * @param id 预约ID
     */
    @Transactional
    public void deleteBooking(Long id) {
        if (!venueBookingRepository.existsById(id)) {
            throw new RuntimeException("预约不存在");
        }
        venueBookingRepository.deleteById(id);
    }

    /**
     * 完成预约
     * @param id 预约ID
     * @return 更新后的预约
     */
    @Transactional
    public VenueBooking completeBooking(Long id) {
        VenueBooking booking = venueBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("预约不存在"));
        booking.setStatus(BookingStatus.COMPLETED);
        return venueBookingRepository.save(booking);
    }

    /**
     * 取消预约
     * @param id 预约ID
     * @return 更新后的预约
     */
    @Transactional
    public VenueBooking cancelBooking(Long id) {
        VenueBooking booking = venueBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("预约不存在"));
        booking.setStatus(BookingStatus.CANCELLED);
        return venueBookingRepository.save(booking);
    }

    /**
     * 检查场地预约冲突
     * @param venueId 场地ID
     * @param date 日期
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 是否存在冲突
     */
    public boolean checkConflict(Long venueId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("场地不存在"));
        return venueBookingRepository.existsConflict(venue, date, startTime, endTime, null);
    }
}
