package com.culturalrelic.service;

import com.culturalrelic.entity.CulturalRelic;
import com.culturalrelic.repository.CulturalRelicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 文物业务逻辑层
 */
@Service
public class CulturalRelicService {

    @Autowired
    private CulturalRelicRepository culturalRelicRepository;

    /**
     * 新增文物
     */
    public CulturalRelic save(CulturalRelic relic) {
        return culturalRelicRepository.save(relic);
    }

    /**
     * 根据ID查询
     */
    public Optional<CulturalRelic> findById(Long id) {
        return culturalRelicRepository.findById(id);
    }

    /**
     * 查询所有（分页）
     */
    public Page<CulturalRelic> findAll(Pageable pageable) {
        return culturalRelicRepository.findAll(pageable);
    }

    /**
     * 查询所有
     */
    public List<CulturalRelic> findAll() {
        return culturalRelicRepository.findAll();
    }

    /**
     * 更新文物
     */
    public CulturalRelic update(CulturalRelic relic) {
        return culturalRelicRepository.save(relic);
    }

    /**
     * 删除文物
     */
    @Transactional
    public boolean delete(Long id) {
        int rows = culturalRelicRepository.logicDelete(id);
        return rows > 0;
    }

    /**
     * 根据文物编号查询
     */
    public CulturalRelic findByRelicNo(String relicNo) {
        return culturalRelicRepository.findByRelicNo(relicNo);
    }

    /**
     * 根据类别查询
     */
    public List<CulturalRelic> findByCategory(String category) {
        return culturalRelicRepository.findByCategory(category);
    }

    /**
     * 根据朝代查询
     */
    public List<CulturalRelic> findByDynasty(String dynasty) {
        return culturalRelicRepository.findByDynasty(dynasty);
    }

    /**
     * 根据名称模糊查询
     */
    public List<CulturalRelic> searchByName(String keyword) {
        return culturalRelicRepository.searchByName(keyword);
    }
}
