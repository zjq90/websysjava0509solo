package com.secondhand.repository;

import com.secondhand.entity.PickupPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 自提点Repository接口
 *
 * @author secondhand
 * @version 1.0.0
 */
@Repository
public interface PickupPointRepository extends JpaRepository<PickupPoint, Long> {

    List<PickupPoint> findByStatusOrderByCreateTimeDesc(String status);

}
