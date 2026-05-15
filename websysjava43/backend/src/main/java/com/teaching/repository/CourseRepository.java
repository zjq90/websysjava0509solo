package com.teaching.repository;

import com.teaching.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByStatusOrderByCreateTimeDesc(Integer status);

    List<Course> findByTeacherIdOrderByCreateTimeDesc(Long teacherId);
}
