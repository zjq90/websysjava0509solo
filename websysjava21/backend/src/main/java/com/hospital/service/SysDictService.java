package com.hospital.service;

import com.hospital.entity.SysDict;
import com.hospital.repository.SysDictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 系统字典服务类
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Service
@Transactional
public class SysDictService {

    @Autowired
    private SysDictRepository dictRepository;

    /**
     * 分页查询字典列表
     */
    public Page<SysDict> findAll(Pageable pageable) {
        return dictRepository.findAll(pageable);
    }

    /**
     * 查询所有字典
     */
    public List<SysDict> findAll() {
        return dictRepository.findAll();
    }

    /**
     * 根据ID查询字典
     */
    public Optional<SysDict> findById(Long id) {
        return dictRepository.findById(id);
    }

    /**
     * 根据字典类型查询字典列表
     */
    public List<SysDict> findByDictType(String dictType) {
        return dictRepository.findByDictTypeOrderBySortOrder(dictType);
    }

    /**
     * 保存字典
     */
    public SysDict save(SysDict dict) {
        return dictRepository.save(dict);
    }

    /**
     * 删除字典
     */
    public void deleteById(Long id) {
        dictRepository.deleteById(id);
    }
}
