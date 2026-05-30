package com.gameplatform.repository;

import com.gameplatform.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByAdSlotId(Long adSlotId);
    List<Advertisement> findByAdSlotIdAndActive(Long adSlotId, Boolean active);
    List<Advertisement> findByActive(Boolean active);
}
