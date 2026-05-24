package com.bikeshare.service;

import com.bikeshare.dto.DashboardDTO;
import com.bikeshare.entity.Area;
import com.bikeshare.repository.AreaRepository;
import com.bikeshare.repository.BikeRepository;
import com.bikeshare.repository.RideRecordRepository;
import com.bikeshare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据看板服务
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RideRecordRepository rideRecordRepository;
    private final UserRepository userRepository;
    private final BikeRepository bikeRepository;
    private final AreaRepository areaRepository;

    public DashboardDTO getDashboardData() {
        DashboardDTO dashboard = new DashboardDTO();

        dashboard.setOperationStats(getOperationStats());
        dashboard.setBikeStatus(getBikeStatusDistribution());
        dashboard.setAreaUsage(getAreaUsage());

        return dashboard;
    }

    private DashboardDTO.OperationStats getOperationStats() {
        DashboardDTO.OperationStats stats = new DashboardDTO.OperationStats();
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        stats.setTodayRides(rideRecordRepository.countRides(todayStart));
        stats.setMonthRides(rideRecordRepository.countRides(monthStart));

        stats.setTodayRevenue(rideRecordRepository.sumRevenue(todayStart));
        stats.setMonthRevenue(rideRecordRepository.sumRevenue(monthStart));

        stats.setTodayNewUsers(userRepository.countNewUsers(todayStart));
        stats.setMonthNewUsers(userRepository.countNewUsers(monthStart));

        return stats;
    }

    private DashboardDTO.BikeStatusDistribution getBikeStatusDistribution() {
        DashboardDTO.BikeStatusDistribution status = new DashboardDTO.BikeStatusDistribution();

        status.setOnlineCount(bikeRepository.countByStatus("ONLINE"));
        status.setOfflineCount(bikeRepository.countByStatus("OFFLINE"));
        status.setFaultCount(bikeRepository.countByStatus("FAULT"));
        status.setMaintenanceCount(bikeRepository.countByStatus("MAINTENANCE"));

        return status;
    }

    private List<DashboardDTO.AreaUsage> getAreaUsage() {
        List<DashboardDTO.AreaUsage> result = new ArrayList<>();
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        List<Area> areas = areaRepository.findByIsActiveTrue();
        List<Object[]> areaRides = rideRecordRepository.countRidesByArea(monthStart);

        for (Area area : areas) {
            DashboardDTO.AreaUsage usage = new DashboardDTO.AreaUsage();
            usage.setAreaName(area.getAreaName());
            usage.setBikeCount(area.getBikeCount());
            usage.setRideCount(0L);

            for (Object[] ride : areaRides) {
                if (area.getAreaName().equals(ride[0])) {
                    usage.setRideCount((Long) ride[1]);
                    break;
                }
            }

            result.add(usage);
        }

        return result;
    }
}
