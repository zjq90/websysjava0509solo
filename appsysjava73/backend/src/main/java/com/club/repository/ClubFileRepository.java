package com.club.repository;

import com.club.entity.ClubFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubFileRepository extends JpaRepository<ClubFile, Long>, JpaSpecificationExecutor<ClubFile> {

    List<ClubFile> findByClubIdAndParentIdAndDeletedFalseOrderByTypeDescCreateTimeDesc(Long clubId, Long parentId);

    List<ClubFile> findByClubIdAndCategoryAndDeletedFalseOrderByCreateTimeDesc(Long clubId, String category);

    List<ClubFile> findByClubIdAndFolderAndFileTypeAndDeletedFalseOrderByCreateTimeDesc(Long clubId, String folder, String fileType);

    List<ClubFile> findByClubIdAndFolderAndDeletedFalseOrderByCreateTimeDesc(Long clubId, String folder);

    List<ClubFile> findByClubIdAndFileTypeAndDeletedFalseOrderByCreateTimeDesc(Long clubId, String fileType);

    List<ClubFile> findByClubIdAndDeletedFalseOrderByCreateTimeDesc(Long clubId);

    Optional<ClubFile> findByIdAndDeletedFalse(Long id);

    @Modifying
    @Query("UPDATE ClubFile f SET f.downloadCount = f.downloadCount + 1 WHERE f.id = :fileId")
    void incrementDownloadCount(@Param("fileId") Long fileId);
}
