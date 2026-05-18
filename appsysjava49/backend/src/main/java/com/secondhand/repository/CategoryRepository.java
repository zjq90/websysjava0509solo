package com.secondhand.repository;

import com.secondhand.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    List<Category> findByParentIdAndStatus(Long parentId, Integer status);

    List<Category> findByLevelAndStatus(Integer level, Integer status);

    List<Category> findByNameContainingAndStatus(String name, Integer status);
}
