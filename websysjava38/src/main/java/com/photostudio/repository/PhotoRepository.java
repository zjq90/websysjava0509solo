package com.photostudio.repository;

import com.photostudio.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 照片数据访问接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>, JpaSpecificationExecutor<Photo> {

    /**
     * 根据相册ID查询所有照片
     */
    List<Photo> findByAlbumIdOrderBySortOrderAsc(Long albumId);

    /**
     * 根据相册ID和选中状态查询
     */
    List<Photo> findByAlbumIdAndIsSelectedOrderBySortOrderAsc(Long albumId, Integer isSelected);

    /**
     * 根据相册ID和标记类型查询
     */
    List<Photo> findByAlbumIdAndMarkTypeOrderBySortOrderAsc(Long albumId, String markType);

    /**
     * 根据修图师ID查询
     */
    List<Photo> findByRetoucherIdOrderByCreateTimeDesc(Long retoucherId);

    /**
     * 根据修图状态查询
     */
    List<Photo> findByRetouchStatusOrderByCreateTimeDesc(Integer retouchStatus);

    /**
     * 统计相册照片数量
     */
    Long countByAlbumId(Long albumId);

    /**
     * 统计相册选中照片数量
     */
    Long countByAlbumIdAndIsSelected(Long albumId, Integer isSelected);
}
