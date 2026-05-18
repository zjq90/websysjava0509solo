package com.heritage.service;

import com.heritage.entity.Heritage;
import com.heritage.entity.User;
import com.heritage.repository.HeritageRepository;
import com.heritage.repository.TransactionRepository;
import com.heritage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private HeritageRepository heritageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Map<String, Object> getHeritageDistributionByProvince() {
        List<Heritage> allHeritage = heritageRepository.findAll();
        Map<String, Object> distribution = new HashMap<>();
        allHeritage.stream()
                .filter(h -> h.getProvince() != null)
                .collect(Collectors.groupingBy(Heritage::getProvince, Collectors.counting()))
                .forEach((k, v) -> distribution.put(k, v));
        return distribution;
    }

    public Map<String, Object> getHeritageDistributionByDynasty() {
        List<Heritage> allHeritage = heritageRepository.findAll();
        Map<String, Object> distribution = new HashMap<>();
        allHeritage.stream()
                .filter(h -> h.getDynasty() != null)
                .collect(Collectors.groupingBy(Heritage::getDynasty, Collectors.counting()))
                .forEach((k, v) -> distribution.put(k, v));
        return distribution;
    }

    public Map<String, Object> getHeritageDistributionByMaterial() {
        List<Heritage> allHeritage = heritageRepository.findAll();
        Map<String, Object> distribution = new HashMap<>();
        allHeritage.stream()
                .filter(h -> h.getMaterial() != null)
                .collect(Collectors.groupingBy(Heritage::getMaterial, Collectors.counting()))
                .forEach((k, v) -> distribution.put(k, v));
        return distribution;
    }

    public Map<String, Object> getUserDistributionByProvince() {
        List<User> allUsers = userRepository.findAll();
        Map<String, Object> distribution = new HashMap<>();
        allUsers.stream()
                .filter(u -> u.getProvince() != null)
                .collect(Collectors.groupingBy(User::getProvince, Collectors.counting()))
                .forEach((k, v) -> distribution.put(k, v));
        return distribution;
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalHeritage", heritageRepository.count());
        stats.put("totalUsers", userRepository.count());
        stats.put("totalTransactions", transactionRepository.count());
        stats.put("totalExperts", userRepository.findByIsExpertTrue().size());
        stats.put("pendingAudit", heritageRepository.findByAuditStatus(
                com.heritage.enums.AuditStatus.PENDING).size());
        return stats;
    }

    public List<Heritage> getHeritageByProvince(String province) {
        return heritageRepository.findByProvince(province);
    }

    public Map<String, Object> getCreditScoreDistribution() {
        List<User> allUsers = userRepository.findAll();
        Map<String, Object> distribution = new HashMap<>();
        distribution.put("优秀(90-100)", allUsers.stream().filter(u -> u.getCreditScore() >= 90).count());
        distribution.put("良好(70-89)", allUsers.stream().filter(u -> u.getCreditScore() >= 70 && u.getCreditScore() < 90).count());
        distribution.put("中等(50-69)", allUsers.stream().filter(u -> u.getCreditScore() >= 50 && u.getCreditScore() < 70).count());
        distribution.put("较差(0-49)", allUsers.stream().filter(u -> u.getCreditScore() < 50).count());
        return distribution;
    }

    public Map<String, Object> getUserGrowthAnalysis(int days) {
        Map<String, Object> result = new HashMap<>();
        List<User> allUsers = userRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        List<String> dateLabels = new ArrayList<>();
        List<Long> dailyNewUsers = new ArrayList<>();
        List<Long> cumulativeUsers = new ArrayList<>();
        
        LocalDate endDate = LocalDate.now();
        long cumulative = 0;
        
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = endDate.minusDays(i);
            String dateStr = date.format(formatter);
            dateLabels.add(dateStr);
            
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
            
            long count = allUsers.stream()
                    .filter(u -> u.getCreateTime() != null 
                            && !u.getCreateTime().isBefore(startOfDay) 
                            && u.getCreateTime().isBefore(endOfDay))
                    .count();
            
            dailyNewUsers.add(count);
            cumulative += count;
            cumulativeUsers.add(cumulative);
        }
        
        long totalUsers = allUsers.size();
        long newUsersToday = dailyNewUsers.get(dailyNewUsers.size() - 1);
        long newUsersYesterday = dailyNewUsers.size() > 1 ? dailyNewUsers.get(dailyNewUsers.size() - 2) : 0;
        double growthRate = newUsersYesterday > 0 
                ? (double) (newUsersToday - newUsersYesterday) / newUsersYesterday * 100 
                : (newUsersToday > 0 ? 100 : 0);
        
        long expertCount = allUsers.stream().filter(User::getIsExpert).count();
        long verifiedCount = allUsers.stream().filter(User::getIdVerified).count();
        
        result.put("dateLabels", dateLabels);
        result.put("dailyNewUsers", dailyNewUsers);
        result.put("cumulativeUsers", cumulativeUsers);
        result.put("totalUsers", totalUsers);
        result.put("newUsersToday", newUsersToday);
        result.put("growthRate", Math.round(growthRate * 100.0) / 100.0);
        result.put("expertCount", expertCount);
        result.put("verifiedCount", verifiedCount);
        result.put("days", days);
        
        return result;
    }

    public Map<String, Object> getUserMonthlyGrowth() {
        Map<String, Object> result = new LinkedHashMap<>();
        List<User> allUsers = userRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        
        Map<String, Long> monthlyCount = allUsers.stream()
                .filter(u -> u.getCreateTime() != null)
                .collect(Collectors.groupingBy(
                        u -> u.getCreateTime().format(formatter),
                        Collectors.counting()
                ));
        
        List<String> months = new ArrayList<>(monthlyCount.keySet());
        months.sort(String::compareTo);
        
        List<Long> counts = new ArrayList<>();
        for (String month : months) {
            counts.add(monthlyCount.get(month));
        }
        
        result.put("months", months);
        result.put("counts", counts);
        return result;
    }
}
