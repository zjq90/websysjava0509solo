package com.gameplatform.service;

import com.gameplatform.entity.PopupAnnouncement;
import com.gameplatform.repository.PopupAnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PopupAnnouncementService {
    @Autowired
    private PopupAnnouncementRepository popupAnnouncementRepository;

    public List<PopupAnnouncement> findAll() {
        return popupAnnouncementRepository.findAll();
    }

    public List<PopupAnnouncement> findAllActive() {
        return popupAnnouncementRepository.findByActive(true);
    }

    public List<PopupAnnouncement> findActiveByTrigger(String trigger) {
        return popupAnnouncementRepository.findByActiveAndDisplayTrigger(true, trigger);
    }

    public Optional<PopupAnnouncement> findById(Long id) {
        return popupAnnouncementRepository.findById(id);
    }

    public PopupAnnouncement save(PopupAnnouncement announcement) {
        return popupAnnouncementRepository.save(announcement);
    }

    public void deleteById(Long id) {
        popupAnnouncementRepository.deleteById(id);
    }

    public PopupAnnouncement update(Long id, PopupAnnouncement announcement) {
        announcement.setId(id);
        return popupAnnouncementRepository.save(announcement);
    }

    public PopupAnnouncement toggleActive(Long id) {
        Optional<PopupAnnouncement> opt = popupAnnouncementRepository.findById(id);
        if (opt.isPresent()) {
            PopupAnnouncement announcement = opt.get();
            announcement.setActive(!announcement.getActive());
            return popupAnnouncementRepository.save(announcement);
        }
        return null;
    }

    public void incrementViewCount(Long id) {
        Optional<PopupAnnouncement> opt = popupAnnouncementRepository.findById(id);
        if (opt.isPresent()) {
            PopupAnnouncement announcement = opt.get();
            announcement.setViewCount(announcement.getViewCount() + 1);
            popupAnnouncementRepository.save(announcement);
        }
    }

    public void incrementClickCount(Long id) {
        Optional<PopupAnnouncement> opt = popupAnnouncementRepository.findById(id);
        if (opt.isPresent()) {
            PopupAnnouncement announcement = opt.get();
            announcement.setClickCount(announcement.getClickCount() + 1);
            popupAnnouncementRepository.save(announcement);
        }
    }
}
