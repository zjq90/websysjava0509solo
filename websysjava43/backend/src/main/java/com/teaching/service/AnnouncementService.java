package com.teaching.service;

import com.teaching.entity.CourseAnnouncement;
import com.teaching.entity.User;
import com.teaching.repository.CourseAnnouncementRepository;
import com.teaching.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnnouncementService {

    @Autowired
    private CourseAnnouncementRepository announcementRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public CourseAnnouncement createAnnouncement(Long courseId, String courseName, String title,
                                                 String content, Long publisherId) {
        User publisher = userRepository.findById(publisherId).orElseThrow(() -> new RuntimeException("用户不存在"));
        
        CourseAnnouncement announcement = new CourseAnnouncement();
        announcement.setCourseId(courseId);
        announcement.setCourseName(courseName);
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setPublisherId(publisherId);
        announcement.setPublisherName(publisher.getRealName() != null ? publisher.getRealName() : publisher.getUsername());
        announcement.setIsTop(false);
        announcement.setStatus(1);
        
        return announcementRepository.save(announcement);
    }

    public List<CourseAnnouncement> getCourseAnnouncements(Long courseId) {
        return announcementRepository.findByCourseIdOrderByIsTopDescCreateTimeDesc(courseId);
    }

    public CourseAnnouncement getById(Long id) {
        return announcementRepository.findById(id).orElseThrow(() -> new RuntimeException("公告不存在"));
    }

    @Transactional
    public CourseAnnouncement updateAnnouncement(Long id, String title, String content) {
        CourseAnnouncement announcement = getById(id);
        if (title != null) announcement.setTitle(title);
        if (content != null) announcement.setContent(content);
        return announcementRepository.save(announcement);
    }

    @Transactional
    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }

    @Transactional
    public CourseAnnouncement toggleTop(Long id) {
        CourseAnnouncement announcement = getById(id);
        announcement.setIsTop(!announcement.getIsTop());
        return announcementRepository.save(announcement);
    }
}
