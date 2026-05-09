package com.agriculture.repository;

import com.agriculture.entity.PestImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 病虫害图片数据访问层
 * 提供病虫害图片表的基本CRUD操作
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Repository
public interface PestImageRepository extends JpaRepository<PestImage, Long> {

    /**
     * 根据田间记录ID查找图片
     * 
     * @param fieldRecordId 田间记录ID
     * @return 图片列表
     */
    List<PestImage> findByFieldRecordIdAndStatusOrderByCreatedAtAsc(Long fieldRecordId, String status);

    /**
     * 根据图片类型查找
     * 
     * @param imageType 图片类型
     * @return 图片列表
     */
    List<PestImage> findByImageTypeAndStatusOrderByCreatedAtDesc(String imageType, String status);

    /**
     * 统计田间记录的图片数量
     * 
     * @param fieldRecordId 田间记录ID
     * @return 图片数量
     */
    long countByFieldRecordIdAndStatus(Long fieldRecordId, String status);
}
