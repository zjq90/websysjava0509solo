package com.club.management.club.repository;

import com.club.management.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 社团Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ClubRepository extends JpaRepository<Club, Long>, JpaSpecificationExecutor<Club> {

    List<Club> findByStatus(Integer status);

    List<Club> findByType(Integer type);

    Club findByName(String name);
}
