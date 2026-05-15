package com.teaching.repository;

import com.teaching.entity.CourseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseUserRepository extends JpaRepository<CourseUser, Long> {

    List<CourseUser> findByUserIdOrderByCreateTimeDesc(Long userId);

    List<CourseUser> findByCourseIdOrderByCreateTimeDesc(Long courseId);

    Optional<CourseUser> findByCourseIdAndUserId(Long courseId, Long userId);

    boolean existsByCourseIdAndUserId(Long courseId, Long userId);
}
