package com.photostudio.repository;

import com.photostudio.entity.Costume;
import com.photostudio.entity.CostumeBooking;
import com.photostudio.entity.CostumeBooking.CostumeBookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 服装预约数据访问接口
 * 提供服装预约相关的数据库操作方法
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface CostumeBookingRepository extends JpaRepository<CostumeBooking, Long> {

    /**
     * 根据服装查询预约列表
     * @param costume 服装
     * @return 预约列表
     */
    List<CostumeBooking> findByCostume(Costume costume);

    /**
     * 根据状态查询预约
     * @param status 状态
     * @return 预约列表
     */
    List<CostumeBooking> findByStatus(CostumeBookingStatus status);

    /**
     * 查询指定日期范围内某服装的预约
     * @param costume 服装
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 预约列表
     */
    List<CostumeBooking> findByCostumeAndUseDateBetween(Costume costume, LocalDate startDate, LocalDate endDate);
}
