package com.heritage.repository;

import com.heritage.entity.Heritage;
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

@Repository
public interface HeritageRepository extends JpaRepository<Heritage, Long>, JpaSpecificationExecutor<Heritage> {

    Optional<Heritage> findByHeritageCode(String heritageCode);

    List<Heritage> findByMasterCode(String masterCode);

    Page<Heritage> findByCategory(String category, Pageable pageable);

    Page<Heritage> findByDynasty(String dynasty, Pageable pageable);

    @Query("SELECT h FROM Heritage h WHERE h.isMasterData = true AND h.deleted = false")
    Page<Heritage> findMasterData(Pageable pageable);

    @Query("SELECT h FROM Heritage h WHERE h.name LIKE %:keyword% OR h.heritageCode LIKE %:keyword% AND h.deleted = false")
    Page<Heritage> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(h) FROM Heritage h WHERE h.category = :category AND h.deleted = false")
    long countByCategory(@Param("category") String category);

    @Query("SELECT COUNT(h) FROM Heritage h WHERE h.dynasty = :dynasty AND h.deleted = false")
    long countByDynasty(@Param("dynasty") String dynasty);

    @Modifying
    @Query("UPDATE Heritage h SET h.deleted = true WHERE h.id = :id")
    void softDeleteById(@Param("id") Long id);

    List<Heritage> findByDataSourceAndDeletedFalse(String dataSource);
}
