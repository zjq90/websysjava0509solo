package com.psyconsult.repository;

import com.psyconsult.entity.CaseTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaseTagRepository extends JpaRepository<CaseTag, Long> {

    Optional<CaseTag> findByName(String name);

    List<CaseTag> findByCategory(String category);

    List<CaseTag> findByStatus(Integer status);
}
