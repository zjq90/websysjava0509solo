package com.photostudio.repository;

import com.photostudio.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单Repository
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 根据状态统计订单数量
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    Long countByStatus(@Param("status") String status);

    /**
     * 根据状态和时间范围统计订单数量
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status AND o.createTime BETWEEN :startTime AND :endTime")
    Long countByStatusAndTimeRange(@Param("status") String status, 
                                     @Param("startTime") LocalDateTime startTime, 
                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 根据状态和时间范围统计订单总金额
     */
    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Order o WHERE o.status IN :status AND o.createTime BETWEEN :startTime AND :endTime")
    BigDecimal sumAmountByStatusAndTimeRange(@Param("status") List<String> status, 
                                           @Param("startTime") LocalDateTime startTime, 
                                           @Param("endTime") LocalDateTime endTime);

    /**
     * 根据门店和时间范围统计订单总金额
     */
    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Order o WHERE o.store.id = :storeId AND o.createTime BETWEEN :startTime AND :endTime")
    BigDecimal sumAmountByStoreAndTimeRange(@Param("storeId") Long storeId, 
                                           @Param("startTime") LocalDateTime startTime, 
                                           @Param("endTime") LocalDateTime endTime);

    /**
     * 根据销售员和时间范围统计订单总金额
     */
    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Order o WHERE o.sales.id = :salesId AND o.createTime BETWEEN :startTime AND :endTime")
    BigDecimal sumAmountBySalesAndTimeRange(@Param("salesId") Long salesId, 
                                           @Param("startTime") LocalDateTime startTime, 
                                           @Param("endTime") LocalDateTime endTime);

    /**
     * 根据套餐类型和时间范围统计订单数量
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.aPackage.type = :packageType AND o.createTime BETWEEN :startTime AND :endTime")
    Long countByPackageTypeAndTimeRange(@Param("packageType") String packageType, 
                                          @Param("startTime") LocalDateTime startTime, 
                                          @Param("endTime") LocalDateTime endTime);

    /**
     * 统计老客户订单数量
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.customer.oldCustomer = true")
    Long countOldCustomerOrders();

    /**
     * 统计转介绍客户订单数量
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.customer.referral IS NOT NULL")
    Long countReferralCustomerOrders();

    /**
     * 根据销售员ID统计订单数量
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.sales.id = :employeeId AND o.createTime BETWEEN :startTime AND :endTime")
    Long countBySalesId(@Param("employeeId") Long employeeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据摄影师ID统计订单数量
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.photographer.id = :employeeId AND o.createTime BETWEEN :startTime AND :endTime")
    Long countByPhotographerId(@Param("employeeId") Long employeeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据化妆师ID统计订单数量
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.makeupArtist.id = :employeeId AND o.createTime BETWEEN :startTime AND :endTime")
    Long countByMakeupArtistId(@Param("employeeId") Long employeeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据选片师ID统计订单数量
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.photoSelector.id = :employeeId AND o.createTime BETWEEN :startTime AND :endTime")
    Long countByPhotoSelectorId(@Param("employeeId") Long employeeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据修图师ID统计订单数量
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.photoEditor.id = :employeeId AND o.createTime BETWEEN :startTime AND :endTime")
    Long countByPhotoEditorId(@Param("employeeId") Long employeeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据销售员ID统计订单金额
     */
    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Order o WHERE o.sales.id = :employeeId AND o.createTime BETWEEN :startTime AND :endTime")
    BigDecimal sumAmountBySalesId(@Param("employeeId") Long employeeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据摄影师ID统计订单金额
     */
    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Order o WHERE o.photographer.id = :employeeId AND o.createTime BETWEEN :startTime AND :endTime")
    BigDecimal sumAmountByPhotographerId(@Param("employeeId") Long employeeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据化妆师ID统计订单金额
     */
    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Order o WHERE o.makeupArtist.id = :employeeId AND o.createTime BETWEEN :startTime AND :endTime")
    BigDecimal sumAmountByMakeupArtistId(@Param("employeeId") Long employeeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据选片师ID统计订单金额
     */
    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Order o WHERE o.photoSelector.id = :employeeId AND o.createTime BETWEEN :startTime AND :endTime")
    BigDecimal sumAmountByPhotoSelectorId(@Param("employeeId") Long employeeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据销售员ID统计平均评分
     */
    @Query("SELECT COALESCE(AVG(o.customerRating), 0) FROM Order o WHERE o.sales.id = :employeeId AND o.customerRating IS NOT NULL AND o.createTime BETWEEN :startTime AND :endTime")
    BigDecimal avgRatingBySalesId(@Param("employeeId") Long employeeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据摄影师ID统计平均评分
     */
    @Query("SELECT COALESCE(AVG(o.customerRating), 0) FROM Order o WHERE o.photographer.id = :employeeId AND o.customerRating IS NOT NULL AND o.createTime BETWEEN :startTime AND :endTime")
    BigDecimal avgRatingByPhotographerId(@Param("employeeId") Long employeeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据化妆师ID统计平均评分
     */
    @Query("SELECT COALESCE(AVG(o.customerRating), 0) FROM Order o WHERE o.makeupArtist.id = :employeeId AND o.customerRating IS NOT NULL AND o.createTime BETWEEN :startTime AND :endTime")
    BigDecimal avgRatingByMakeupArtistId(@Param("employeeId") Long employeeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据选片师ID统计平均评分
     */
    @Query("SELECT COALESCE(AVG(o.customerRating), 0) FROM Order o WHERE o.photoSelector.id = :employeeId AND o.customerRating IS NOT NULL AND o.createTime BETWEEN :startTime AND :endTime")
    BigDecimal avgRatingByPhotoSelectorId(@Param("employeeId") Long employeeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
