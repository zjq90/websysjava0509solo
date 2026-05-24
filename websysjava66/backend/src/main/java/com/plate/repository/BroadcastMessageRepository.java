package com.plate.repository;

import com.plate.entity.BroadcastMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BroadcastMessageRepository extends JpaRepository<BroadcastMessage, Long> {
}
