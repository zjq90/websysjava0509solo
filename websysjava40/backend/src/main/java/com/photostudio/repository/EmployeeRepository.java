package com.photostudio.repository;

import com.photostudio.entity.Employee;
import com.photostudio.entity.Employee.EmployeePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 员工数据访问接口
 * 提供员工相关的数据库操作方法
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * 根据员工工号查询员工
     * @param employeeNo 员工工号
     * @return 员工信息
     */
    Optional<Employee> findByEmployeeNo(String employeeNo);

    /**
     * 根据职位查询在职员工
     * @param position 职位
     * @return 员工列表
     */
    List<Employee> findByPositionAndActiveTrue(EmployeePosition position);

    /**
     * 查询所有在职员工
     * @return 员工列表
     */
    List<Employee> findByActiveTrue();

    /**
     * 根据姓名模糊查询员工
     * @param name 姓名关键词
     * @return 员工列表
     */
    List<Employee> findByNameContaining(String name);
}
