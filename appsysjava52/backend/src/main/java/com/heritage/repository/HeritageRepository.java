package com.heritage.repository;

import com.heritage.entity.Heritage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文物Repository
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Repository
public interface HeritageRepository extends JpaRepository<Heritage, Long> {

    /**
     * 根据类别查询文物
     */
    Page<Heritage> findByCategoryAndStatusAndDeleted(Integer category, Integer status, Integer deleted, Pageable pageable);

    /**
     * 查询公开的文物
     */
    Page<Heritage> findByIsPublicAndStatusAndDeleted(Integer isPublic, Integer status, Integer deleted, Pageable pageable);

    /**
     * 根据名称模糊查询
     */
    Page<Heritage> findByNameContainingAndStatusAndDeleted(String name, Integer status, Integer deleted, Pageable pageable);

    /**
     * 增加浏览次数
     */
    @Modifying
    @Query("UPDATE Heritage h SET h.viewCount = h.viewCount + 1 WHERE h.id = ?1")
    void incrementViewCount(Long id);

    /**
     * 增加收藏次数
     */
    @Modifying
    @Query("UPDATE Heritage h SET h.favoriteCount = h.favoriteCount + 1 WHERE h.id = ?1")
    void incrementFavoriteCount(Long id);

    /**
     * 减少收藏次数
     */
    @Modifying
    @Query("UPDATE Heritage h SET h.favoriteCount = h.favoriteCount - 1 WHERE h.id = ?1 AND h.favoriteCount > 0")
    void decrementFavoriteCount(Long id);

    /**
     * 查询热门文物（按浏览量）
     */
    List<Heritage> findTop10ByStatusAndDeletedOrderByViewCountDesc(Integer status, Integer deleted);
}