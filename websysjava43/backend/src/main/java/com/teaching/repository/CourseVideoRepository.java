package com.teaching.repository;

import com.teaching.entity.CourseVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseVideoRepository extends JpaRepository<CourseVideo, Long> {

    List<CourseVideo> findByCourseIdOrderByCreateTimeDesc(Long courseId);
}
