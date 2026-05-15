package com.teaching.repository;

import com.teaching.entity.CourseComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseCommentRepository extends JpaRepository<CourseComment, Long> {

    List<CourseComment> findByCourseIdOrderByCreateTimeDesc(Long courseId);
}
