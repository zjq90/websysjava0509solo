package com.club.management.square.repository;

import com.club.management.square.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 话题Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>, JpaSpecificationExecutor<Topic> {

    List<Topic> findByStatusOrderByIsTopDescFollowCountDesc(Integer status);

    Page<Topic> findByCategoryAndStatusOrderByIsTopDescFollowCountDesc(Integer category, Integer status, Pageable pageable);

    List<Topic> findByIsHotAndStatus(Integer isHot, Integer status);
}
