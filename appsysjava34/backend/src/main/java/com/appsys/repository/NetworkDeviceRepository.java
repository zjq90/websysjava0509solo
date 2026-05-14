package com.appsys.repository;

import com.appsys.entity.NetworkDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NetworkDeviceRepository extends JpaRepository<NetworkDevice, Long> {

    List<NetworkDevice> findByUserId(Long userId);

    Optional<NetworkDevice> findByDeviceId(String deviceId);
}
