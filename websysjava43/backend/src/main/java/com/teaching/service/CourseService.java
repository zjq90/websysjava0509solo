package com.teaching.service;

import com.teaching.entity.Course;
import com.teaching.entity.CourseUser;
import com.teaching.entity.User;
import com.teaching.repository.CourseRepository;
import com.teaching.repository.CourseUserRepository;
import com.teaching.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseUserRepository courseUserRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findByStatusOrderByCreateTimeDesc(1);
    }

    public Course getById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new RuntimeException("课程不存在"));
    }

    @Transactional
    public Course createCourse(Course course, Long teacherId) {
        User teacher = userRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("教师不存在"));
        course.setTeacherId(teacherId);
        course.setTeacherName(teacher.getRealName() != null ? teacher.getRealName() : teacher.getUsername());
        course.setStudentCount(0);
        course.setStatus(1);
        Course savedCourse = courseRepository.save(course);
        
        CourseUser courseUser = new CourseUser();
        courseUser.setCourseId(savedCourse.getId());
        courseUser.setCourseName(savedCourse.getName());
        courseUser.setUserId(teacherId);
        courseUser.setUserName(teacher.getRealName() != null ? teacher.getRealName() : teacher.getUsername());
        courseUser.setUserRole(0);
        courseUserRepository.save(courseUser);
        
        return savedCourse;
    }

    @Transactional
    public Course updateCourse(Long id, Course course) {
        Course existingCourse = getById(id);
        if (course.getName() != null) existingCourse.setName(course.getName());
        if (course.getDescription() != null) existingCourse.setDescription(course.getDescription());
        if (course.getCover() != null) existingCourse.setCover(course.getCover());
        if (course.getCapacity() != null) existingCourse.setCapacity(course.getCapacity());
        return courseRepository.save(existingCourse);
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = getById(id);
        course.setStatus(0);
        courseRepository.save(course);
    }

    public List<Course> getUserCourses(Long userId) {
        List<CourseUser> courseUsers = courseUserRepository.findByUserIdOrderByCreateTimeDesc(userId);
        return courseUsers.stream()
                .map(cu -> courseRepository.findById(cu.getCourseId()).orElse(null))
                .filter(c -> c != null && c.getStatus() == 1)
                .collect(Collectors.toList());
    }

    @Transactional
    public void enrollCourse(Long courseId, Long userId) {
        Course course = getById(courseId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        
        if (courseUserRepository.existsByCourseIdAndUserId(courseId, userId)) {
            throw new RuntimeException("已选修该课程");
        }
        
        if (course.getCapacity() != null && course.getStudentCount() >= course.getCapacity()) {
            throw new RuntimeException("课程人数已满");
        }
        
        CourseUser courseUser = new CourseUser();
        courseUser.setCourseId(courseId);
        courseUser.setCourseName(course.getName());
        courseUser.setUserId(userId);
        courseUser.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
        courseUser.setUserRole(1);
        courseUserRepository.save(courseUser);
        
        course.setStudentCount(course.getStudentCount() + 1);
        courseRepository.save(course);
    }

    @Transactional
    public void dropCourse(Long courseId, Long userId) {
        Course course = getById(courseId);
        CourseUser courseUser = courseUserRepository.findByCourseIdAndUserId(courseId, userId)
                .orElseThrow(() -> new RuntimeException("未选修该课程"));
        
        courseUserRepository.delete(courseUser);
        course.setStudentCount(Math.max(0, course.getStudentCount() - 1));
        courseRepository.save(course);
    }

    public List<User> getCourseStudents(Long courseId) {
        List<CourseUser> courseUsers = courseUserRepository.findByCourseIdOrderByCreateTimeDesc(courseId);
        return courseUsers.stream()
                .filter(cu -> cu.getUserRole() == 1)
                .map(cu -> userRepository.findById(cu.getUserId()).orElse(null))
                .filter(u -> u != null)
                .collect(Collectors.toList());
    }

    public boolean isEnrolled(Long courseId, Long userId) {
        return courseUserRepository.existsByCourseIdAndUserId(courseId, userId);
    }
}
