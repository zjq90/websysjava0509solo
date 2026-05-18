package com.culturalrelic.repository;

import com.culturalrelic.entity.VrScene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * VR场景数据访问层
 */
@Repository
public interface VrSceneRepository extends JpaRepository<VrScene, Long>, JpaSpecificationExecutor<VrScene> {

    /**
     * 根据场景编号查询
     */
    VrScene findBySceneNo(String sceneNo);

    /**
     * 根据场景类型查询
     */
    List<VrScene> findBySceneType(Integer sceneType);

    /**
     * 根据状态查询
     */
    List<VrScene> findByStatus(Integer status);

    /**
     * 逻辑删除
     */
    @Modifying
    @Query("UPDATE VrScene s SET s.deleted = 1 WHERE s.id = :id")
    int logicDelete(@Param("id") Long id);
}
