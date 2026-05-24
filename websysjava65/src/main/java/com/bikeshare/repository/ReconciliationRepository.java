package com.bikeshare.repository;

import com.bikeshare.entity.Reconciliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 对账记录数据访问层
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Repository
public interface ReconciliationRepository extends JpaRepository<Reconciliation, Long> {

    List<Reconciliation> findByStatus(String status);

    Optional<Reconciliation> findByReconDateAndReconType(LocalDate reconDate, String reconType);
}
