package com.psyconsult.repository;

import com.psyconsult.entity.Supervision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupervisionRepository extends JpaRepository<Supervision, Long> {

    List<Supervision> findByCounselorId(Long counselorId);

    List<Supervision> findBySupervisorId(Long supervisorId);

    List<Supervision> findByCounselorIdAndStatus(Long counselorId, String status);

    List<Supervision> findBySupervisorIdAndStatus(Long supervisorId, String status);
}
