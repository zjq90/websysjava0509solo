package com.vehicle.repository;

import com.vehicle.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {

    Optional<Blacklist> findByPlateNumberAndEnabledTrue(String plateNumber);

    boolean existsByPlateNumberAndEnabledTrue(String plateNumber);
}
