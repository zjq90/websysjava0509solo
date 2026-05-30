package com.gameplatform.repository;

import com.gameplatform.entity.PopupAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PopupAnnouncementRepository extends JpaRepository<PopupAnnouncement, Long> {
    List<PopupAnnouncement> findByActive(Boolean active);
    List<PopupAnnouncement> findByActiveAndDisplayTrigger(Boolean active, String displayTrigger);
}
