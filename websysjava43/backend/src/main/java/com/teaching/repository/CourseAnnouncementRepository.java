package com.teaching.repository;

import com.teaching.entity.CourseAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseAnnouncementRepository extends JpaRepository<CourseAnnouncement, Long> {

    List<CourseAnnouncement> findByCourseIdOrderByIsTopDescCreateTimeDesc(Long courseId);
}
