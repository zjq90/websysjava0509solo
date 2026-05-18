package com.flower.repository;

import com.flower.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 公告数据访问层
 */
@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long>, JpaSpecificationExecutor<Announcement> {

    /**
     * 根据状态查询公告
     */
    List<Announcement> findByStatusOrderByTopFlagDescCreateTimeDesc(Integer status);

    /**
     * 根据类型查询公告
     */
    List<Announcement> findByType(Integer type);
}
