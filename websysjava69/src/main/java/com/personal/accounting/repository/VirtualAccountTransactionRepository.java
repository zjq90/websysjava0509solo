package com.personal.accounting.repository;

import com.personal.accounting.entity.VirtualAccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VirtualAccountTransactionRepository extends JpaRepository<VirtualAccountTransaction, Long> {
    List<VirtualAccountTransaction> findByVirtualAccountIdOrderByTransactionDateDesc(Long virtualAccountId);
    List<VirtualAccountTransaction> findByUserIdOrderByTransactionDateDesc(Long userId);
}
