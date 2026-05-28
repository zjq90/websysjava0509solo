package com.club.management.resource.repository;

import com.club.management.resource.entity.ResourceShare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 资源分享Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ResourceShareRepository extends JpaRepository<ResourceShare, Long>, JpaSpecificationExecutor<ResourceShare> {

    Page<ResourceShare> findByIsPublicAndStatusOrderByIsTopDescCreateTimeDesc(Integer isPublic, Integer status, Pageable pageable);

    Page<ResourceShare> findByCategoryAndIsPublicAndStatusOrderByIsTopDescCreateTimeDesc(Integer category, Integer isPublic, Integer status, Pageable pageable);

    Page<ResourceShare> findByClubIdAndStatusOrderByIsTopDescCreateTimeDesc(Long clubId, Integer status, Pageable pageable);

    Page<ResourceShare> findByUploaderIdOrderByCreateTimeDesc(Long uploaderId, Pageable pageable);

    @Modifying
    @Query("UPDATE ResourceShare r SET r.downloadCount = r.downloadCount + 1 WHERE r.id = :id")
    int increaseDownloadCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE ResourceShare r SET r.viewCount = r.viewCount + 1 WHERE r.id = :id")
    int increaseViewCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE ResourceShare r SET r.likeCount = r.likeCount + 1 WHERE r.id = :id")
    int increaseLikeCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE ResourceShare r SET r.favoriteCount = r.favoriteCount + 1 WHERE r.id = :id")
    int increaseFavoriteCount(@Param("id") Long id);
}
