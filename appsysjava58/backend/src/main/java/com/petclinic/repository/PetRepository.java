package com.petclinic.repository;

import com.petclinic.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 宠物数据访问接口
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByUserIdAndDeletedFalse(Long userId);

    List<Pet> findByUserIdAndTypeAndDeletedFalse(Long userId, String type);

    List<Pet> findBySmartCollarIdIsNotNullAndDeletedFalse();
}