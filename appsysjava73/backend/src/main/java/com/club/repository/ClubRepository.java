package com.club.repository;

import com.club.entity.Club;
import com.club.entity.enums.ClubCategoryEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 社团Repository接口
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ClubRepository extends JpaRepository<Club, Long>, JpaSpecificationExecutor<Club> {

    Page<Club> findByStatusAndDeletedFalseOrderByViewCountDesc(Integer status, Pageable pageable);

    Page<Club> findByCategoryAndStatusAndDeletedFalseOrderByViewCountDesc(ClubCategoryEnum category, Integer status, Pageable pageable);

    Page<Club> findBySchoolIdAndStatusAndDeletedFalseOrderByViewCountDesc(Long schoolId, Integer status, Pageable pageable);

    Page<Club> findByCategoryAndSchoolIdAndStatusAndDeletedFalseOrderByViewCountDesc(ClubCategoryEnum category, Long schoolId, Integer status, Pageable pageable);

    Page<Club> findByNameContainingAndStatusAndDeletedFalseOrderByViewCountDesc(String name, Integer status, Pageable pageable);

    Optional<Club> findByIdAndDeletedFalse(Long id);

    Optional<Club> findByNameAndDeletedFalse(String name);

    boolean existsByNameAndDeletedFalse(String name);

    List<Club> findByLeaderIdAndDeletedFalse(Long leaderId);

    @Modifying
    @Query("UPDATE Club c SET c.viewCount = c.viewCount + 1 WHERE c.id = :clubId")
    void incrementViewCount(@Param("clubId") Long clubId);

    @Modifying
    @Query("UPDATE Club c SET c.followCount = c.followCount + 1 WHERE c.id = :clubId")
    void incrementFollowCount(@Param("clubId") Long clubId);

    @Modifying
    @Query("UPDATE Club c SET c.followCount = c.followCount - 1 WHERE c.id = :clubId")
    void decrementFollowCount(@Param("clubId") Long clubId);

    @Modifying
    @Query("UPDATE Club c SET c.memberCount = c.memberCount + 1 WHERE c.id = :clubId")
    void incrementMemberCount(@Param("clubId") Long clubId);

    @Modifying
    @Query("UPDATE Club c SET c.memberCount = c.memberCount - 1 WHERE c.id = :clubId")
    void decrementMemberCount(@Param("clubId") Long clubId);
}
