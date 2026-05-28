package com.club.service;

import com.club.entity.Club;
import com.club.entity.ClubActivity;
import com.club.entity.ClubMember;
import com.club.entity.enums.MemberRoleEnum;
import com.club.exception.BusinessException;
import com.club.repository.ClubActivityRepository;
import com.club.repository.ClubMemberRepository;
import com.club.repository.ClubRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatisticsService {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private ClubActivityRepository clubActivityRepository;

    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Map<String, Object> getClubStatistics(Long clubId, Long userId) {
        log.info("获取社团统计数据 - clubId: {}, userId: {}", clubId, userId);

        checkClubPermission(clubId, userId);

        Map<String, Object> result = new HashMap<>();

        long totalMembers = clubMemberRepository.countByClubIdAndDeletedFalse(clubId);
        long activeMembers = clubMemberRepository.countByClubIdAndActiveAndDeletedFalse(clubId, 1);
        long totalActivities = clubActivityRepository.countByClubIdAndDeletedFalse(clubId);
        long finishedActivities = clubActivityRepository.countByClubIdAndStatusAndDeletedFalse(clubId, 2);

        result.put("totalMembers", totalMembers);
        result.put("activeMembers", activeMembers);
        result.put("totalActivities", totalActivities);
        result.put("finishedActivities", finishedActivities);

        return result;
    }

    public Map<String, Object> getMemberGrowthTrend(Long clubId, Long userId) {
        log.info("获取成员增长趋势 - clubId: {}, userId: {}", clubId, userId);

        checkClubPermission(clubId, userId);

        List<ClubMember> members = clubMemberRepository.findByClubIdAndDeletedFalse(clubId);

        Map<String, Integer> monthlyGrowth = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();

        for (int i = 11; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            String monthKey = month.format(MONTH_FORMATTER);
            monthlyGrowth.put(monthKey, 0);
        }

        for (ClubMember member : members) {
            if (member.getJoinTime() != null && !member.getJoinTime().isEmpty()) {
                try {
                    LocalDateTime joinDateTime = LocalDateTime.parse(member.getJoinTime(), DATETIME_FORMATTER);
                    LocalDate joinDate = joinDateTime.toLocalDate();
                    String monthKey = joinDate.format(MONTH_FORMATTER);
                    if (monthlyGrowth.containsKey(monthKey)) {
                        monthlyGrowth.put(monthKey, monthlyGrowth.get(monthKey) + 1);
                    }
                } catch (Exception e) {
                    log.warn("解析成员加入时间失败: {}", member.getJoinTime());
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("months", new ArrayList<>(monthlyGrowth.keySet()));
        result.put("counts", new ArrayList<>(monthlyGrowth.values()));

        return result;
    }

    public Map<String, Object> getActivityParticipationRate(Long clubId, Long userId) {
        log.info("获取活动参与率 - clubId: {}, userId: {}", clubId, userId);

        checkClubPermission(clubId, userId);

        List<ClubActivity> activities = clubActivityRepository.findByClubIdAndStatusAndDeletedFalseOrderByCreateTimeDesc(clubId, 2);

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> activityList = new ArrayList<>();

        long totalMembers = clubMemberRepository.countByClubIdAndDeletedFalse(clubId);

        int totalActivities = 0;
        int totalParticipants = 0;

        for (ClubActivity activity : activities) {
            if (totalActivities >= 6) break;
            totalActivities++;
            totalParticipants += activity.getParticipantCount() != null ? activity.getParticipantCount() : 0;

            Map<String, Object> activityData = new HashMap<>();
            activityData.put("id", activity.getId());
            activityData.put("title", activity.getTitle());
            activityData.put("participantCount", activity.getParticipantCount());
            activityData.put("maxParticipants", activity.getMaxParticipants());
            activityData.put("rate", totalMembers > 0
                    ? Math.round((double) (activity.getParticipantCount() != null ? activity.getParticipantCount() : 0) / totalMembers * 100)
                    : 0);

            String dateStr = "";
            if (activity.getStartTime() != null && !activity.getStartTime().isEmpty()) {
                try {
                    LocalDateTime startTime = LocalDateTime.parse(activity.getStartTime(), DATETIME_FORMATTER);
                    dateStr = startTime.format(DATE_FORMATTER);
                } catch (Exception e) {
                    log.warn("解析活动开始时间失败: {}", activity.getStartTime());
                    dateStr = activity.getStartTime();
                }
            }
            activityData.put("date", dateStr);
            activityList.add(activityData);
        }

        double avgRate = totalActivities > 0 && totalMembers > 0
                ? Math.round((double) totalParticipants / (totalActivities * totalMembers) * 100)
                : 0;

        result.put("activities", activityList);
        result.put("avgRate", avgRate);
        result.put("totalActivities", totalActivities);
        result.put("totalParticipants", totalParticipants);

        return result;
    }

    public Map<String, Object> generateMonthlyReport(Long clubId, int year, int month, Long userId) {
        log.info("生成月度活动报告 - clubId: {}, year: {}, month: {}, userId: {}", clubId, year, month, userId);

        checkClubPermission(clubId, userId);

        Map<String, Object> report = new HashMap<>();

        List<ClubMember> allMembers = clubMemberRepository.findByClubIdAndDeletedFalse(clubId);
        List<ClubActivity> allActivities = clubActivityRepository.findByClubIdAndDeletedFalse(clubId);

        String monthPrefix = String.format("%04d-%02d", year, month);

        List<ClubMember> newMembersThisMonth = allMembers.stream()
                .filter(m -> {
                    if (m.getJoinTime() == null || m.getJoinTime().isEmpty()) return false;
                    try {
                        LocalDateTime joinTime = LocalDateTime.parse(m.getJoinTime(), DATETIME_FORMATTER);
                        return joinTime.format(MONTH_FORMATTER).equals(monthPrefix);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        List<ClubActivity> activitiesThisMonth = allActivities.stream()
                .filter(a -> {
                    if (a.getStartTime() == null || a.getStartTime().isEmpty()) return false;
                    try {
                        LocalDateTime startTime = LocalDateTime.parse(a.getStartTime(), DATETIME_FORMATTER);
                        return startTime.format(MONTH_FORMATTER).equals(monthPrefix);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        long totalParticipantsThisMonth = activitiesThisMonth.stream()
                .mapToLong(a -> a.getParticipantCount() != null ? a.getParticipantCount() : 0)
                .sum();

        long finishedActivities = activitiesThisMonth.stream()
                .filter(a -> a.getStatus() != null && a.getStatus() == 2)
                .count();

        report.put("year", year);
        report.put("month", month);
        report.put("reportTitle", String.format("%d年%d月社团活动报告", year, month));

        Map<String, Object> memberStats = new HashMap<>();
        memberStats.put("totalMembers", allMembers.size());
        memberStats.put("newMembers", newMembersThisMonth.size());
        memberStats.put("activeMembers", allMembers.stream().filter(m -> m.getActive() != null && m.getActive() == 1).count());
        report.put("memberStats", memberStats);

        Map<String, Object> activityStats = new HashMap<>();
        activityStats.put("totalActivities", activitiesThisMonth.size());
        activityStats.put("finishedActivities", finishedActivities);
        activityStats.put("totalParticipants", totalParticipantsThisMonth);
        activityStats.put("avgParticipants", activitiesThisMonth.size() > 0 ? totalParticipantsThisMonth / activitiesThisMonth.size() : 0);
        report.put("activityStats", activityStats);

        List<Map<String, Object>> activityList = new ArrayList<>();
        for (ClubActivity activity : activitiesThisMonth) {
            Map<String, Object> a = new HashMap<>();
            a.put("id", activity.getId());
            a.put("title", activity.getTitle());
            a.put("type", activity.getType());
            a.put("location", activity.getLocation());

            String startTimeStr = "";
            if (activity.getStartTime() != null && !activity.getStartTime().isEmpty()) {
                try {
                    LocalDateTime startTime = LocalDateTime.parse(activity.getStartTime(), DATETIME_FORMATTER);
                    startTimeStr = startTime.format(DATE_FORMATTER);
                } catch (Exception e) {
                    startTimeStr = activity.getStartTime();
                }
            }
            a.put("startTime", startTimeStr);
            a.put("participantCount", activity.getParticipantCount());
            a.put("maxParticipants", activity.getMaxParticipants());
            a.put("status", activity.getStatus() == 0 ? "未开始" : activity.getStatus() == 1 ? "进行中" : "已结束");
            activityList.add(a);
        }
        report.put("activities", activityList);

        List<Map<String, Object>> topMembers = new ArrayList<>();
        List<ClubMember> sortedByActivity = allMembers.stream()
                .sorted((m1, m2) -> Integer.compare(
                        m2.getActivityCount() != null ? m2.getActivityCount() : 0,
                        m1.getActivityCount() != null ? m1.getActivityCount() : 0))
                .limit(5)
                .collect(Collectors.toList());

        for (ClubMember member : sortedByActivity) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", member.getId());
            m.put("realName", member.getRealName());
            m.put("departmentName", member.getDepartmentName());
            m.put("activityCount", member.getActivityCount());
            topMembers.add(m);
        }
        report.put("topMembers", topMembers);

        return report;
    }

    public Map<String, Object> getMemberDepartmentDistribution(Long clubId, Long userId) {
        log.info("获取成员部门分布 - clubId: {}, userId: {}", clubId, userId);

        checkClubPermission(clubId, userId);

        List<ClubMember> members = clubMemberRepository.findByClubIdAndDeletedFalse(clubId);

        Map<String, Integer> deptCount = new HashMap<>();
        Map<String, Map<String, Integer>> roleCountByDept = new HashMap<>();

        for (ClubMember member : members) {
            String dept = member.getDepartmentName() != null ? member.getDepartmentName() : "未分配";
            String role = member.getRole().getDesc();

            deptCount.put(dept, deptCount.getOrDefault(dept, 0) + 1);

            roleCountByDept.computeIfAbsent(dept, k -> new HashMap<>());
            roleCountByDept.get(dept).put(role, roleCountByDept.get(dept).getOrDefault(role, 0) + 1);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("departments", new ArrayList<>(deptCount.keySet()));
        result.put("counts", new ArrayList<>(deptCount.values()));
        result.put("roleDistribution", roleCountByDept);

        return result;
    }

    private void checkClubPermission(Long clubId, Long userId) {
        ClubMember operator = clubMemberRepository.findByClubIdAndUserIdAndDeletedFalse(clubId, userId)
                .orElseThrow(() -> new BusinessException("你不是该社团成员"));

        if (operator.getRole() != MemberRoleEnum.PRESIDENT && operator.getRole() != MemberRoleEnum.VICE_PRESIDENT) {
            throw new BusinessException("权限不足，只有社长或副社长可以查看统计数据");
        }
    }
}
