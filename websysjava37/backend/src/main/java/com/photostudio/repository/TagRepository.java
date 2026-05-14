package com.photostudio.repository;

import com.photostudio.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 标签数据访问层
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    /**
     * 根据标签名称查询
     */
    Optional<Tag> findByNameAndDeletedFalse(String name);

    /**
     * 查询所有未删除的标签
     */
    List<Tag> findAllByDeletedFalse();

    /**
     * 查询自动标签
     */
    List<Tag> findByAutoTagAndDeletedFalse(Boolean autoTag);

    /**
     * 检查标签名称是否已存在
     */
    boolean existsByNameAndDeletedFalse(String name);
}
