package com.secondhand.repository;

import com.secondhand.entity.BargainHelp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 砍价助力Repository接口
 *
 * @author secondhand
 * @version 1.0.0
 */
@Repository
public interface BargainHelpRepository extends JpaRepository<BargainHelp, Long> {

    List<BargainHelp> findByRecordIdOrderByCreateTimeDesc(Long recordId);

    boolean existsByRecordIdAndHelperId(Long recordId, Long helperId);

}
