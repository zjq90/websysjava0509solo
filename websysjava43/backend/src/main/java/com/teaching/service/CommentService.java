package com.teaching.service;

import com.teaching.entity.CourseComment;
import com.teaching.entity.User;
import com.teaching.repository.CourseCommentRepository;
import com.teaching.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CourseCommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public CourseComment createComment(Long courseId, String courseName, String content, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        
        CourseComment comment = new CourseComment();
        comment.setCourseId(courseId);
        comment.setCourseName(courseName);
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
        comment.setAvatar(user.getAvatar());
        comment.setStatus(1);
        
        return commentRepository.save(comment);
    }

    public List<CourseComment> getCourseComments(Long courseId) {
        return commentRepository.findByCourseIdOrderByCreateTimeDesc(courseId);
    }

    public CourseComment getById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("评论不存在"));
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
