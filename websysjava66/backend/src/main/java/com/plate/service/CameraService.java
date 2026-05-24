package com.plate.service;

import com.plate.entity.Camera;
import com.plate.repository.CameraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CameraService {
    @Autowired
    private CameraRepository cameraRepository;

    public List<Camera> getAllCameras() {
        return cameraRepository.findAll();
    }

    public List<Camera> getOnlineCameras() {
        return cameraRepository.findByIsOnlineTrue();
    }

    public Optional<Camera> getCameraById(Long id) {
        return cameraRepository.findById(id);
    }

    public Optional<Camera> getCameraByCameraId(String cameraId) {
        return cameraRepository.findByCameraId(cameraId);
    }

    public Camera saveCamera(Camera camera) {
        return cameraRepository.save(camera);
    }

    public void deleteCamera(Long id) {
        cameraRepository.deleteById(id);
    }

    public void setAllNightMode(boolean nightMode) {
        List<Camera> cameras = cameraRepository.findAll();
        for (Camera camera : cameras) {
            camera.setNightMode(nightMode);
            cameraRepository.save(camera);
        }
    }

    public long getTotalCameras() {
        return cameraRepository.count();
    }

    public long getOnlineCameraCount() {
        return cameraRepository.countByIsOnlineTrue();
    }
}
