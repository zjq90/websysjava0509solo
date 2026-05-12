package com.lims.repository;

import com.lims.entity.TestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 检验检查项目数据访问层
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Repository
public interface TestItemRepository extends JpaRepository<TestItem, Long> {

    /**
     * 根据项目编号查询
     */
    TestItem findByItemCode(String itemCode);

    /**
     * 根据项目类型查询
     */
    List<TestItem> findByItemType(String itemType);

    /**
     * 根据所属科室ID查询
     */
    List<TestItem> findByDepartmentId(Long departmentId);

    /**
     * 根据状态查询
     */
    List<TestItem> findByStatus(String status);

    /**
     * 根据项目类型和状态查询
     */
    List<TestItem> findByItemTypeAndStatus(String itemType, String status);
}
