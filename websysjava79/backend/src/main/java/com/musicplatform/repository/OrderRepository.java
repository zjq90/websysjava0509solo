package com.musicplatform.repository;

import com.musicplatform.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNo(String orderNo);

    Page<Order> findByUser_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<Order> findByArtistIdOrderByCreatedAtDesc(Long artistId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Order o WHERE o.artistId = :artistId AND o.status = 'PAID'")
    double calculateTotalRevenue(@Param("artistId") Long artistId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.musicId = :musicId AND o.status = 'PAID'")
    long countPurchasesByMusicId(@Param("musicId") Long musicId);
}
