package com.referee.repository;

import com.referee.entity.Referee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 裁判数据访问接口
 *
 * @author Referee System
 * @version 1.0.0
 */
@Repository
public interface RefereeRepository extends JpaRepository<Referee, Long> {

    /**
     * 根据裁判编号查询
     *
     * @param refereeNo 裁判编号
     * @return 裁判信息
     */
    Optional<Referee> findByRefereeNo(String refereeNo);

    /**
     * 根据姓名查询
     *
     * @param name 姓名
     * @return 裁判列表
     */
    List<Referee> findByNameContaining(String name);

    /**
     * 查询裁判长列表
     *
     * @param isChief 是否为裁判长
     * @return 裁判列表
     */
    List<Referee> findByIsChief(Integer isChief);

    /**
     * 根据状态查询
     *
     * @param status 状态
     * @return 裁判列表
     */
    List<Referee> findByStatus(Integer status);

    /**
     * 根据裁判长标志和状态查询
     *
     * @param isChief 是否为裁判长
     * @param status  状态
     * @return 裁判列表
     */
    List<Referee> findByIsChiefAndStatus(Integer isChief, Integer status);

    /**
     * 根据专业领域查询
     *
     * @param specialty 专业领域
     * @return 裁判列表
     */
    List<Referee> findBySpecialtyContaining(String specialty);

    /**
     * 检查裁判编号是否存在
     *
     * @param refereeNo 裁判编号
     * @return 是否存在
     */
    boolean existsByRefereeNo(String refereeNo);

    /**
     * 模糊查询
     *
     * @param keyword 关键词
     * @return 裁判列表
     */
    @Query("SELECT r FROM Referee r WHERE r.refereeNo LIKE %?1% OR r.name LIKE %?1% OR r.organization LIKE %?1%")
    List<Referee> findByKeyword(String keyword);
}
