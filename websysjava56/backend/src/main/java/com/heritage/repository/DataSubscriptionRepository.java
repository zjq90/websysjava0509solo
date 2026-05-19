package com.heritage.repository;

import com.heritage.entity.DataSubscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSubscriptionRepository extends JpaRepository<DataSubscription, Long>, JpaSpecificationExecutor<DataSubscription> {

    List<DataSubscription> findBySubscriberId(Long subscriberId);

    Page<DataSubscription> findByTopic(String topic, Pageable pageable);

    List<DataSubscription> findByTopicAndStatus(String topic, String status);

    List<DataSubscription> findByStatus(String status);
}
