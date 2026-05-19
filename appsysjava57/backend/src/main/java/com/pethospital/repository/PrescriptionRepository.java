package com.pethospital.repository;

import com.pethospital.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处方数据访问接口
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByUserIdOrderByCreateTimeDesc(Long userId);
}
