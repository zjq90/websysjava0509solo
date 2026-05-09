package com.agriculture.repository;

import com.agriculture.entity.Plot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 地块数据访问层
 * 提供地块表的基本CRUD操作
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Repository
public interface PlotRepository extends JpaRepository<Plot, Long> {

    /**
     * 根据地块编号查找
     * 
     * @param plotCode 地块编号
     * @return 地块对象
     */
    Optional<Plot> findByPlotCode(String plotCode);

    /**
     * 根据状态查找地块列表
     * 
     * @param status 状态
     * @return 地块列表
     */
    List<Plot> findByStatusOrderByCreatedAtDesc(String status);

    /**
     * 根据城市查找地块
     * 
     * @param city 城市
     * @return 地块列表
     */
    List<Plot> findByCity(String city);

    /**
     * 检查地块编号是否存在
     * 
     * @param plotCode 地块编号
     * @return 是否存在
     */
    boolean existsByPlotCode(String plotCode);
}
