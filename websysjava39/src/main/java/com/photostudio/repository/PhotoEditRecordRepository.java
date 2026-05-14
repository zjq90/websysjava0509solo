package com.photostudio.repository;

import com.photostudio.entity.PhotoEditRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 修图记录Repository
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Repository
public interface PhotoEditRecordRepository extends JpaRepository<PhotoEditRecord, Long> {
    
    /**
     * 根据修图师ID和时间范围统计修图数量
     */
    @Query("SELECT COALESCE(SUM(per.photoCount), 0) FROM PhotoEditRecord per " +
           "WHERE per.editor.id = :editorId AND per.createTime BETWEEN :startTime AND :endTime")
    Integer sumPhotoCountByEditorAndTimeRange(@Param("editorId") Long editorId,
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);
                                              
    /**
     * 根据修图师ID和时间范围统计返修数量
     */
    @Query("SELECT COALESCE(SUM(per.reworkCount), 0) FROM PhotoEditRecord per " +
           "WHERE per.editor.id = :editorId AND per.createTime BETWEEN :startTime AND :endTime")
    Integer sumReworkCountByEditorAndTimeRange(@Param("editorId") Long editorId,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);
}
