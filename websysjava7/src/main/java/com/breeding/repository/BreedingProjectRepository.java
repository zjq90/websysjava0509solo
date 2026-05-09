package com.breeding.repository;

import com.breeding.entity.BreedingProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 育种项目数据访问接口
 * 继承JpaRepository，提供基本的CRUD操作
 */
@Repository
public interface BreedingProjectRepository extends JpaRepository<BreedingProject, Long> {

    /**
     * 根据项目编号查询
     */
    BreedingProject findByProjectCode(String projectCode);

    /**
     * 根据项目状态查询
     */
    List<BreedingProject> findByStatus(String status);

    /**
     * 根据作物类型查询
     */
    List<BreedingProject> findByCropType(String cropType);

    /**
     * 根据负责人查询
     */
    List<BreedingProject> findByResponsiblePerson(String responsiblePerson);

    /**
     * 模糊查询项目名称或编号
     */
    @Query("SELECT p FROM BreedingProject p WHERE p.projectName LIKE %?1% OR p.projectCode LIKE %?1%")
    List<BreedingProject> searchByKeyword(String keyword);

    /**
     * 查询进行中的项目数量
     */
    @Query("SELECT COUNT(p) FROM BreedingProject p WHERE p.status = '进行中'")
    Long countActiveProjects();
}
