package com.heritage.repository;

import com.heritage.entity.CategoryTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryTagRepository extends JpaRepository<CategoryTag, Long> {

    List<CategoryTag> findByTypeOrderBySortOrderAsc(String type);

    List<CategoryTag> findByEnabledTrueOrderBySortOrderAsc();

    CategoryTag findByTypeAndName(String type, String name);
}
