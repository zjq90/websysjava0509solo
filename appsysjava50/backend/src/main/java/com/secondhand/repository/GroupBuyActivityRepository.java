package com.secondhand.repository;

import com.secondhand.entity.GroupBuyActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 拼团活动Repository接口
 *
 * @author secondhand
 * @version 1.0.0
 */
@Repository
public interface GroupBuyActivityRepository extends JpaRepository<GroupBuyActivity, Long> {

    List<GroupBuyActivity> findByStatusOrderByCreateTimeDesc(String status);

}
