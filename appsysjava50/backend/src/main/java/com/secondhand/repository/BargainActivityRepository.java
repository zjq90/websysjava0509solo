package com.secondhand.repository;

import com.secondhand.entity.BargainActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 砍价活动Repository接口
 *
 * @author secondhand
 * @version 1.0.0
 */
@Repository
public interface BargainActivityRepository extends JpaRepository<BargainActivity, Long> {

    List<BargainActivity> findByStatusOrderByCreateTimeDesc(String status);

}
