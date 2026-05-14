package com.photostudio.repository;

import com.photostudio.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 员工数据访问接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    /**
     * 根据职位查询员工
     */
    List<Employee> findByPosition(String position);

    /**
     * 根据状态查询员工
     */
    List<Employee> findByStatusOrderBySortOrderAsc(Integer status);

    /**
     * 查询在职员工
     */
    List<Employee> findByStatusAndPositionOrderBySortOrderAsc(Integer status, String position);

    /**
     * 根据专长查询（模糊匹配）
     */
    List<Employee> findBySpecialtyContaining(String specialty);
}
