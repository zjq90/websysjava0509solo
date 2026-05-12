package com.hospital.repository;

import com.hospital.entity.SysDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统字典数据访问层
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Repository
public interface SysDictRepository extends JpaRepository<SysDict, Long>, JpaSpecificationExecutor<SysDict> {

    /**
     * 根据字典类型查询字典列表
     */
    List<SysDict> findByDictTypeOrderBySortOrder(String dictType);

    /**
     * 根据字典类型和编码查询字典
     */
    SysDict findByDictTypeAndDictCode(String dictType, String dictCode);

    /**
     * 查询所有字典类型（去重）
     */
    List<SysDict> findDistinctDictTypeBy();
}
