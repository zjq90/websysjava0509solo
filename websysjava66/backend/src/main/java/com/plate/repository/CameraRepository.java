package com.plate.repository;

import com.plate.entity.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CameraRepository extends JpaRepository<Camera, Long> {
    Optional<Camera> findByCameraId(String cameraId);
    long countByIsOnlineTrue();
    List<Camera> findByIsOnlineTrue();
}
