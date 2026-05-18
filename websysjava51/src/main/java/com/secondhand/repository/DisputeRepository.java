package com.secondhand.repository;

import com.secondhand.entity.Dispute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisputeRepository extends JpaRepository<Dispute, Long>, JpaSpecificationExecutor<Dispute> {

    List<Dispute> findByComplainantId(Long complainantId);

    List<Dispute> findByRespondentId(Long respondentId);

    List<Dispute> findByStatus(String status);

    Dispute findByOrderId(Long orderId);

}