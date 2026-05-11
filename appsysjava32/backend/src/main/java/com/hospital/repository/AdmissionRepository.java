package com.hospital.repository;

import com.hospital.entity.Admission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Long>, JpaSpecificationExecutor<Admission> {
    Page<Admission> findByUserIdOrderByCreateTimeDesc(Long userId, Pageable pageable);
    List<Admission> findByUserIdOrderByCreateTimeDesc(Long userId);
    Optional<Admission> findByApplicationNo(String applicationNo);
    List<Admission> findByUserIdAndStatusIn(Long userId, List<Integer> statusList);
}
