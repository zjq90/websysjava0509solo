package com.appsys.repository;

import com.appsys.entity.WiFiSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WiFiSettingRepository extends JpaRepository<WiFiSetting, Long> {

    Optional<WiFiSetting> findByUserId(Long userId);
}
