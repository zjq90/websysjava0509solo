package com.petclinic.repository;

import com.petclinic.entity.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 宠物主人Repository
 */
@Repository
public interface PetOwnerRepository extends JpaRepository<PetOwner, Long> {

    /**
     * 根据手机号查询
     */
    PetOwner findByPhone(String phone);

    /**
     * 根据实名认证状态查询
     */
    List<PetOwner> findByRealNameStatus(Integer realNameStatus);

    /**
     * 根据信用分范围查询
     */
    List<PetOwner> findByCreditScoreBetween(Integer minScore, Integer maxScore);

    /**
     * 根据创建时间范围查询（用户增长分析）
     */
    List<PetOwner> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
