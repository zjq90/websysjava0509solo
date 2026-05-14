package com.photostudio.repository;

import com.photostudio.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 门店Repository
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    
    List<Store> findByActive(Boolean active);
}
