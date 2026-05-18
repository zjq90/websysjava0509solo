package com.psyconsult.repository;

import com.psyconsult.entity.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Long> {
    Optional<Counselor> findByUsername(String username);
    List<Counselor> findByIsAvailableTrueAndEnabledTrue();
    
    @Query("SELECT c FROM Counselor c WHERE c.isAvailable = true AND c.enabled = true " +
           "AND (:specialty IS NULL OR :specialty MEMBER OF c.specialties) " +
           "AND (:language IS NULL OR :language MEMBER OF c.languages) " +
           "AND (:minPrice IS NULL OR c.pricePerHour >= :minPrice) " +
           "AND (:maxPrice IS NULL OR c.pricePerHour <= :maxPrice)")
    List<Counselor> findByFilters(
            @Param("specialty") String specialty,
            @Param("language") String language,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice);
}
