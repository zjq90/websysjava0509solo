package com.photostudio.repository;

import com.photostudio.entity.Costume;
import com.photostudio.entity.Costume.CleaningStatus;
import com.photostudio.entity.Costume.CostumeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 服装数据访问接口
 * 提供服装相关的数据库操作方法
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface CostumeRepository extends JpaRepository<Costume, Long> {

    /**
     * 根据服装编号查询服装
     * @param costumeNo 服装编号
     * @return 服装信息
     */
    Costume findByCostumeNo(String costumeNo);

    /**
     * 根据类型查询服装
     * @param type 服装类型
     * @return 服装列表
     */
    List<Costume> findByType(CostumeType type);

    /**
     * 根据清洗状态查询服装
     * @param cleaningStatus 清洗状态
     * @return 服装列表
     */
    List<Costume> findByCleaningStatus(CleaningStatus cleaningStatus);

    /**
     * 查询可用的服装
     * @return 服装列表
     */
    List<Costume> findByAvailableTrue();

    /**
     * 查询可用且已清洁的服装
     * @param available 是否可用
     * @param cleaningStatus 清洗状态
     * @return 服装列表
     */
    List<Costume> findByAvailableTrueAndCleaningStatus(CleaningStatus cleaningStatus);

    /**
     * 根据名称模糊查询服装
     * @param name 名称关键词
     * @return 服装列表
     */
    List<Costume> findByNameContaining(String name);

    /**
     * 根据类型和可用状态查询服装
     * @param type 服装类型
     * @param available 是否可用
     * @return 服装列表
     */
    List<Costume> findByTypeAndAvailableTrue(CostumeType type);
}
