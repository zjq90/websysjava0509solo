package com.flower.service;

import com.flower.entity.Announcement;
import com.flower.entity.Article;
import com.flower.repository.AnnouncementRepository;
import com.flower.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 内容管理服务类
 */
@Service
public class ContentService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private ArticleRepository articleRepository;

    // ========== 公告管理 ==========

    public Page<Announcement> findAllAnnouncements(Pageable pageable) {
        return announcementRepository.findAll(pageable);
    }

    public Announcement findAnnouncementById(Long id) {
        return announcementRepository.findById(id).orElse(null);
    }

    @Transactional
    public Announcement saveAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    @Transactional
    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }

    @Transactional
    public Announcement publishAnnouncement(Long id) {
        Announcement announcement = findAnnouncementById(id);
        if (announcement != null) {
            announcement.setStatus(1);
            announcement.setPublishTime(LocalDateTime.now());
            return announcementRepository.save(announcement);
        }
        return null;
    }

    @Transactional
    public Announcement toggleTop(Long id) {
        Announcement announcement = findAnnouncementById(id);
        if (announcement != null) {
            announcement.setTopFlag(announcement.getTopFlag() == 1 ? 0 : 1);
            return announcementRepository.save(announcement);
        }
        return null;
    }

    public List<Announcement> getPublishedAnnouncements() {
        return announcementRepository.findByStatusOrderByTopFlagDescCreateTimeDesc(1);
    }

    // ========== 文章管理 ==========

    public Page<Article> findAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    public Article findArticleById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    @Transactional
    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    @Transactional
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    @Transactional
    public Article publishArticle(Long id) {
        Article article = findArticleById(id);
        if (article != null) {
            article.setStatus(1);
            article.setPublishTime(LocalDateTime.now());
            return articleRepository.save(article);
        }
        return null;
    }

    @Transactional
    public Article incrementViewCount(Long id) {
        Article article = findArticleById(id);
        if (article != null) {
            article.setViewCount(article.getViewCount() + 1);
            return articleRepository.save(article);
        }
        return null;
    }

    public List<Article> getPublishedArticles() {
        return articleRepository.findByStatus(1);
    }

    public List<Article> getArticlesByCategory(Integer category) {
        return articleRepository.findByCategory(category);
    }
}
