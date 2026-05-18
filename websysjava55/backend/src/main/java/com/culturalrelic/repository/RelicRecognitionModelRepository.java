package com.culturalrelic.repository;

import com.culturalrelic.entity.RelicRecognitionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文物识别模型数据访问层
 */
@Repository
public interface RelicRecognitionModelRepository extends JpaRepository<RelicRecognitionModel, Long>, JpaSpecificationExecutor<RelicRecognitionModel> {

    /**
     * 根据模型编号查询
     */
    RelicRecognitionModel findByModelNo(String modelNo);

    /**
     * 根据状态查询
     */
    List<RelicRecognitionModel> findByStatus(Integer status);

    /**
     * 查询默认模型
     */
    RelicRecognitionModel findByIsDefault(Integer isDefault);

    /**
     * 逻辑删除
     */
    @Modifying
    @Query("UPDATE RelicRecognitionModel m SET m.deleted = 1 WHERE m.id = :id")
    int logicDelete(@Param("id") Long id);
}
