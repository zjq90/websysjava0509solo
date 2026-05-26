package com.personal.accounting.repository;

import com.personal.accounting.entity.VirtualAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VirtualAccountRepository extends JpaRepository<VirtualAccount, Long> {
    List<VirtualAccount> findByUserId(Long userId);
    List<VirtualAccount> findByFamilyId(Long familyId);
    List<VirtualAccount> findByUserIdOrFamilyId(Long userId, Long familyId);
}
