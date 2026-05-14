package com.appsys.repository;

import com.appsys.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

    List<ServiceOrder> findByUserIdOrderByCreateTimeDesc(Long userId);

    Optional<ServiceOrder> findByOrderNo(String orderNo);
}
