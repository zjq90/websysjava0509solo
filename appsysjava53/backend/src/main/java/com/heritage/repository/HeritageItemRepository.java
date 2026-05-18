package com.heritage.repository;

import com.heritage.entity.HeritageItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文物数据访问接口
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Repository
public interface HeritageItemRepository extends JpaRepository<HeritageItem, Long> {

    List<HeritageItem> findByUserId(Long userId);

    Page<HeritageItem> findByIsOnSaleAndStatus(Integer isOnSale, Integer status, Pageable pageable);

    Page<HeritageItem> findByCategoryAndIsOnSale(String category, Integer isOnSale, Pageable pageable);

    @Query("SELECT h FROM HeritageItem h WHERE h.name LIKE %?1% OR h.description LIKE %?1%")
    Page<HeritageItem> searchByKeyword(String keyword, Pageable pageable);

    List<HeritageItem> findTop10ByOrderByViewCountDesc();
}
