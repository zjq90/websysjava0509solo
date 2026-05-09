package com.agriculture.service;

import com.agriculture.entity.Plot;
import com.agriculture.repository.PlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 地块服务类
 * 提供地块管理的业务逻辑
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Service
@Transactional
public class PlotService {

    @Autowired
    private PlotRepository plotRepository;

    /**
     * 创建地块
     * 
     * @param plot 地块信息
     * @return 创建后的地块
     */
    public Plot create(Plot plot) {
        if (plotRepository.existsByPlotCode(plot.getPlotCode())) {
            throw new RuntimeException("地块编号已存在");
        }
        return plotRepository.save(plot);
    }

    /**
     * 更新地块信息
     * 
     * @param plot 地块信息
     * @return 更新后的地块
     */
    public Plot update(Plot plot) {
        if (!plotRepository.existsById(plot.getId())) {
            throw new RuntimeException("地块不存在");
        }
        return plotRepository.save(plot);
    }

    /**
     * 根据ID删除地块
     * 
     * @param id 地块ID
     */
    public void deleteById(Long id) {
        plotRepository.deleteById(id);
    }

    /**
     * 根据ID查找地块
     * 
     * @param id 地块ID
     * @return 地块对象
     */
    public Optional<Plot> findById(Long id) {
        return plotRepository.findById(id);
    }

    /**
     * 根据地块编号查找
     * 
     * @param plotCode 地块编号
     * @return 地块对象
     */
    public Optional<Plot> findByPlotCode(String plotCode) {
        return plotRepository.findByPlotCode(plotCode);
    }

    /**
     * 查询所有地块
     * 
     * @return 地块列表
     */
    public List<Plot> findAll() {
        return plotRepository.findAll();
    }

    /**
     * 查询启用状态的地块
     * 
     * @return 地块列表
     */
    public List<Plot> findActivePlots() {
        return plotRepository.findByStatusOrderByCreatedAtDesc("ACTIVE");
    }

    /**
     * 根据城市查找地块
     * 
     * @param city 城市
     * @return 地块列表
     */
    public List<Plot> findByCity(String city) {
        return plotRepository.findByCity(city);
    }

    /**
     * 检查地块编号是否存在
     * 
     * @param plotCode 地块编号
     * @return 是否存在
     */
    public boolean existsByPlotCode(String plotCode) {
        return plotRepository.existsByPlotCode(plotCode);
    }
}
