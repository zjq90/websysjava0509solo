package com.bikeshare.service;

import com.bikeshare.entity.CustomerTicket;
import com.bikeshare.entity.User;
import com.bikeshare.entity.UserBlacklist;
import com.bikeshare.entity.UserMembership;
import com.bikeshare.repository.CustomerTicketRepository;
import com.bikeshare.repository.UserBlacklistRepository;
import com.bikeshare.repository.UserMembershipRepository;
import com.bikeshare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 用户管理服务
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserBlacklistRepository userBlacklistRepository;
    private final CustomerTicketRepository customerTicketRepository;
    private final UserMembershipRepository userMembershipRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<UserBlacklist> getBlacklist() {
        return userBlacklistRepository.findAll();
    }

    @Transactional
    public UserBlacklist addToBlacklist(Long userId, String reason, String operator) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (userBlacklistRepository.existsByUserId(userId)) {
            throw new RuntimeException("用户已在黑名单中");
        }

        user.setIsBlacklisted(true);
        userRepository.save(user);

        UserBlacklist blacklist = new UserBlacklist();
        blacklist.setUserId(userId);
        blacklist.setUsername(user.getUsername());
        blacklist.setPhone(user.getPhone());
        blacklist.setReason(reason);
        blacklist.setOperator(operator);

        return userBlacklistRepository.save(blacklist);
    }

    @Transactional
    public void removeFromBlacklist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        user.setIsBlacklisted(false);
        userRepository.save(user);

        userBlacklistRepository.findByUserId(userId)
                .ifPresent(userBlacklistRepository::delete);
    }

    public List<CustomerTicket> getTickets(String status) {
        if (status != null && !status.isEmpty()) {
            return customerTicketRepository.findByStatus(status);
        }
        return customerTicketRepository.findAll();
    }

    public CustomerTicket getTicket(Long id) {
        return customerTicketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("工单不存在"));
    }

    @Transactional
    public CustomerTicket handleTicket(Long id, String reply, String handler, String status) {
        CustomerTicket ticket = getTicket(id);
        ticket.setReply(reply);
        ticket.setHandler(handler);
        ticket.setStatus(status);
        ticket.setHandleTime(LocalDateTime.now());
        ticket.setUpdateTime(LocalDateTime.now());
        return customerTicketRepository.save(ticket);
    }

    public List<User> getHighFrequencyUsers(Integer minRides) {
        return userRepository.findHighFrequencyUsers(minRides);
    }

    @Transactional
    public UserMembership giftMonthlyCard(Long userId, String operator) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (user.getIsVip()) {
            throw new RuntimeException("用户已是会员");
        }

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMonths(1);

        user.setIsVip(true);
        user.setVipExpireTime(endTime);
        userRepository.save(user);

        UserMembership membership = new UserMembership();
        membership.setUserId(userId);
        membership.setUsername(user.getUsername());
        membership.setOrderNo("VIP" + System.currentTimeMillis());
        membership.setPlanType("MONTHLY");
        membership.setDays(30);
        membership.setAmount(java.math.BigDecimal.ZERO);
        membership.setSource("GIFTED");
        membership.setRemark("运营活动赠送 - " + operator);
        membership.setStartTime(startTime);
        membership.setEndTime(endTime);

        return userMembershipRepository.save(membership);
    }

    public List<UserMembership> getMembershipHistory(Long userId) {
        return userMembershipRepository.findByUserId(userId);
    }
}
