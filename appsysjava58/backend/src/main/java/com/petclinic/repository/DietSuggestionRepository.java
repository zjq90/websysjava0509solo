package com.petclinic.repository;

import com.petclinic.entity.DietSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 饮食建议数据访问接口
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Repository
public interface DietSuggestionRepository extends JpaRepository<DietSuggestion, Long> {

    List<DietSuggestion> findByPetIdAndDeletedFalseOrderByGeneratedTimeDesc(Long petId);
}