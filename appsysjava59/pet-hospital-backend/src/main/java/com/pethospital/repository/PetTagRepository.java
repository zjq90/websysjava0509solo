package com.pethospital.repository;

import com.pethospital.entity.PetTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PetTagRepository extends JpaRepository<PetTag, Long> {
    List<PetTag> findByPetId(Long petId);
}
