package com.culturalrelic.service;

import com.culturalrelic.entity.VrScene;
import com.culturalrelic.repository.VrSceneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * VR场景业务逻辑层
 */
@Service
public class VrSceneService {

    @Autowired
    private VrSceneRepository vrSceneRepository;

    public VrScene save(VrScene scene) {
        return vrSceneRepository.save(scene);
    }

    public Optional<VrScene> findById(Long id) {
        return vrSceneRepository.findById(id);
    }

    public Page<VrScene> findAll(Pageable pageable) {
        return vrSceneRepository.findAll(pageable);
    }

    public List<VrScene> findAll() {
        return vrSceneRepository.findAll();
    }

    public VrScene update(VrScene scene) {
        return vrSceneRepository.save(scene);
    }

    @Transactional
    public boolean delete(Long id) {
        int rows = vrSceneRepository.logicDelete(id);
        return rows > 0;
    }

    public VrScene findBySceneNo(String sceneNo) {
        return vrSceneRepository.findBySceneNo(sceneNo);
    }

    public List<VrScene> findBySceneType(Integer sceneType) {
        return vrSceneRepository.findBySceneType(sceneType);
    }

    public List<VrScene> findByStatus(Integer status) {
        return vrSceneRepository.findByStatus(status);
    }
}
