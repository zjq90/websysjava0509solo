package com.appsys.repository;

import com.appsys.entity.PointGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointGoodsRepository extends JpaRepository<PointGoods, Long> {

    List<PointGoods> findByStatusOrderBySortAsc(Integer status);

    List<PointGoods> findByTypeAndStatusOrderBySortAsc(Integer type, Integer status);

    List<PointGoods> findByMinMemberLevelLessThanEqualAndStatusOrderBySortAsc(Integer level, Integer status);
}
