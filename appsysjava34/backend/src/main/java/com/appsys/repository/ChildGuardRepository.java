package com.appsys.repository;

import com.appsys.entity.ChildGuard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChildGuardRepository extends JpaRepository<ChildGuard, Long> {

    List<ChildGuard> findByUserId(Long userId);
}
