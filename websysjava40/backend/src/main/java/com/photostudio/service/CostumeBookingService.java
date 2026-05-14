package com.photostudio.service;

import com.photostudio.entity.Costume;
import com.photostudio.entity.CostumeBooking;
import com.photostudio.entity.CostumeBooking.CostumeBookingStatus;
import com.photostudio.repository.CostumeBookingRepository;
import com.photostudio.repository.CostumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 服装预约服务类
 * 提供服装预约管理相关的业务逻辑
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
public class CostumeBookingService {

    private final CostumeBookingRepository costumeBookingRepository;
    private final CostumeRepository costumeRepository;

    @Autowired
    public CostumeBookingService(CostumeBookingRepository costumeBookingRepository, CostumeRepository costumeRepository) {
        this.costumeBookingRepository = costumeBookingRepository;
        this.costumeRepository = costumeRepository;
    }

    /**
     * 获取所有服装预约
     * @return 预约列表
     */
    public List<CostumeBooking> getAllBookings() {
        return costumeBookingRepository.findAll();
    }

    /**
     * 根据ID获取预约
     * @param id 预约ID
     * @return 预约信息
     */
    public Optional<CostumeBooking> getBookingById(Long id) {
        return costumeBookingRepository.findById(id);
    }

    /**
     * 根据服装获取预约
     * @param costumeId 服装ID
     * @return 预约列表
     */
    public List<CostumeBooking> getBookingsByCostume(Long costumeId) {
        Costume costume = costumeRepository.findById(costumeId)
                .orElseThrow(() -> new RuntimeException("服装不存在"));
        return costumeBookingRepository.findByCostume(costume);
    }

    /**
     * 根据状态获取预约
     * @param status 状态
     * @return 预约列表
     */
    public List<CostumeBooking> getBookingsByStatus(CostumeBookingStatus status) {
        return costumeBookingRepository.findByStatus(status);
    }

    /**
     * 创建服装预约
     * @param booking 预约信息
     * @return 创建的预约
     */
    @Transactional
    public CostumeBooking createBooking(CostumeBooking booking) {
        Costume costume = costumeRepository.findById(booking.getCostume().getId())
                .orElseThrow(() -> new RuntimeException("服装不存在"));
        
        if (!costume.getAvailable()) {
            throw new RuntimeException("服装不可用");
        }
        
        booking.setCostume(costume);
        CostumeBooking saved = costumeBookingRepository.save(booking);
        
        costume.setAvailable(false);
        costume.setUseCount(costume.getUseCount() + 1);
        costume.setCleaningStatus(Costume.CleaningStatus.TO_BE_CLEANED);
        costumeRepository.save(costume);
        
        return saved;
    }

    /**
     * 更新服装预约
     * @param id 预约ID
     * @param booking 预约信息
     * @return 更新后的预约
     */
    @Transactional
    public CostumeBooking updateBooking(Long id, CostumeBooking booking) {
        CostumeBooking existing = costumeBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("预约不存在"));
        
        if (booking.getCostume() != null && booking.getCostume().getId() != null) {
            Costume costume = costumeRepository.findById(booking.getCostume().getId())
                    .orElseThrow(() -> new RuntimeException("服装不存在"));
            existing.setCostume(costume);
        }
        
        existing.setCustomerName(booking.getCustomerName());
        existing.setCustomerPhone(booking.getCustomerPhone());
        existing.setUseDate(booking.getUseDate());
        existing.setReturnDate(booking.getReturnDate());
        existing.setStatus(booking.getStatus());
        existing.setRemark(booking.getRemark());
        
        return costumeBookingRepository.save(existing);
    }

    /**
     * 删除服装预约
     * @param id 预约ID
     */
    @Transactional
    public void deleteBooking(Long id) {
        CostumeBooking booking = costumeBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("预约不存在"));
        
        Costume costume = booking.getCostume();
        costume.setAvailable(true);
        costumeRepository.save(costume);
        
        costumeBookingRepository.deleteById(id);
    }

    /**
     * 标记服装已归还
     * @param id 预约ID
     * @return 更新后的预约
     */
    @Transactional
    public CostumeBooking returnCostume(Long id) {
        CostumeBooking booking = costumeBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("预约不存在"));
        
        booking.setStatus(CostumeBookingStatus.RETURNED);
        booking.setReturnDate(LocalDate.now());
        
        Costume costume = booking.getCostume();
        costume.setAvailable(true);
        costumeRepository.save(costume);
        
        return costumeBookingRepository.save(booking);
    }

    /**
     * 取消预约
     * @param id 预约ID
     * @return 更新后的预约
     */
    @Transactional
    public CostumeBooking cancelBooking(Long id) {
        CostumeBooking booking = costumeBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("预约不存在"));
        
        booking.setStatus(CostumeBookingStatus.CANCELLED);
        
        Costume costume = booking.getCostume();
        costume.setAvailable(true);
        costumeRepository.save(costume);
        
        return costumeBookingRepository.save(booking);
    }
}
