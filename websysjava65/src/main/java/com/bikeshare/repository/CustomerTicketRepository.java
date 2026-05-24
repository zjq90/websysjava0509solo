package com.bikeshare.repository;

import com.bikeshare.entity.CustomerTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客服工单数据访问层
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Repository
public interface CustomerTicketRepository extends JpaRepository<CustomerTicket, Long> {

    List<CustomerTicket> findByStatus(String status);

    List<CustomerTicket> findByType(String type);

    List<CustomerTicket> findByUserId(Long userId);
}
