package com.flower.repository;

import com.flower.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章数据访问层
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    /**
     * 根据状态查询文章
     */
    List<Article> findByStatus(Integer status);

    /**
     * 根据分类查询文章
     */
    List<Article> findByCategory(Integer category);

    /**
     * 根据标题模糊查询
     */
    List<Article> findByTitleContaining(String title);
}
