package com.accounting.repository;

import com.accounting.entity.BillTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillTemplateRepository extends JpaRepository<BillTemplate, Long> {

    List<BillTemplate> findAllByOrderByUseCountDesc();
}
