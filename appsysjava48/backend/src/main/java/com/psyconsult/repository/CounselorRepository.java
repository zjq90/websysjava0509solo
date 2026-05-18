package com.psyconsult.repository;

import com.psyconsult.entity.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Long> {

    List<Counselor> findByStatus(Integer status);

    List<Counselor> findByIsSeniorAndStatus(Boolean isSenior, Integer status);

    List<Counselor> findByNameContaining(String keyword);

    Counselor findByUserId(Long userId);
}
