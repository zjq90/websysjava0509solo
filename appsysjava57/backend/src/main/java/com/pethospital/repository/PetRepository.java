package com.pethospital.repository;

import com.pethospital.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 宠物数据访问接口
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByUserId(Long userId);

    List<Pet> findByUserIdOrderByCreateTimeDesc(Long userId);
}
