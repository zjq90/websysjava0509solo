package com.club.repository;

import com.club.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 学校Repository接口
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface SchoolRepository extends JpaRepository<School, Long>, JpaSpecificationExecutor<School> {

    List<School> findByStatusAndDeletedFalseOrderByIdAsc(Integer status);

    Optional<School> findByIdAndDeletedFalse(Long id);

    Optional<School> findByNameAndDeletedFalse(String name);
}
