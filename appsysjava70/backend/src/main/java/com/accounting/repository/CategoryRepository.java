package com.accounting.repository;

import com.accounting.entity.Category;
import com.accounting.enums.BillType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByTypeOrderBySortOrderAsc(BillType type);

    Optional<Category> findByNameAndType(String name, BillType type);

    List<Category> findAllByOrderBySortOrderAsc();
}
