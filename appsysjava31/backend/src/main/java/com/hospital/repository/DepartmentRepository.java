package com.hospital.repository;

import com.hospital.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 科室Repository接口
 * 提供科室数据访问层的基本操作
 * 
 * @author hospital
 * @version 1.0.0
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

    /**
     * 根据科室编号查询
     * 
     * @param deptCode 科室编号
     * @return 科室对象
     */
    Optional<Department> findByDeptCode(String deptCode);

    /**
     * 查询所有启用的科室，按排序号排序
     * 
     * @return 科室列表
     */
    List<Department> findByStatusOrderBySortOrderAsc(Integer status);

    /**
     * 根据父科室ID查询子科室
     * 
     * @param parentId 父科室ID
     * @return 子科室列表
     */
    List<Department> findByParentIdAndStatusOrderBySortOrderAsc(Long parentId, Integer status);
}
