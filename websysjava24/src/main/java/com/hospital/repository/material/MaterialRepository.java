package com.hospital.repository.material;

import com.hospital.entity.material.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long>, JpaSpecificationExecutor<Material> {

    Optional<Material> findByMaterialCode(String materialCode);

    List<Material> findByStatus(Integer status);

    List<Material> findByCategory(String category);

    List<Material> findByIsHighValue(Boolean isHighValue);

    boolean existsByMaterialCode(String materialCode);
}
