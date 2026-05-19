package com.petclinic.service;

import com.petclinic.dto.Result;
import com.petclinic.entity.HospitalRating;
import com.petclinic.entity.PetHospital;
import com.petclinic.repository.HospitalRatingRepository;
import com.petclinic.repository.PetHospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 宠物医院服务类
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalService {

    private final PetHospitalRepository petHospitalRepository;
    private final HospitalRatingRepository hospitalRatingRepository;

    /**
     * 获取附近的宠物医院
     * 
     * @param latitude 当前纬度
     * @param longitude 当前经度
     * @param radius 搜索半径（公里）
     * @return 医院列表
     */
    public Result<List<PetHospital>> getNearbyHospitals(BigDecimal latitude, BigDecimal longitude, double radius) {
        double latRange = radius / 111.0;
        double lngRange = radius / (111.0 * Math.cos(Math.toRadians(latitude.doubleValue())));

        BigDecimal minLat = latitude.subtract(BigDecimal.valueOf(latRange));
        BigDecimal maxLat = latitude.add(BigDecimal.valueOf(latRange));
        BigDecimal minLng = longitude.subtract(BigDecimal.valueOf(lngRange));
        BigDecimal maxLng = longitude.add(BigDecimal.valueOf(lngRange));

        List<PetHospital> hospitals = petHospitalRepository.findNearbyHospitals(minLat, maxLat, minLng, maxLng);
        
        hospitals.forEach(hospital -> {
            double distance = calculateDistance(
                latitude.doubleValue(), longitude.doubleValue(),
                hospital.getLatitude().doubleValue(), hospital.getLongitude().doubleValue()
            );
            hospital.setDescription("距离约" + String.format("%.2f", distance) + "公里");
        });

        return Result.success(hospitals);
    }

    /**
     * 计算两点之间的距离（Haversine公式）
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * 获取医院详情
     */
    public Result<PetHospital> getHospitalDetail(Long hospitalId) {
        Optional<PetHospital> hospitalOpt = petHospitalRepository.findById(hospitalId);
        if (hospitalOpt.isEmpty()) {
            return Result.error("医院不存在");
        }
        return Result.success(hospitalOpt.get());
    }

    /**
     * 提交医院评分
     */
    @Transactional
    public Result<HospitalRating> submitRating(HospitalRating rating) {
        HospitalRating saved = hospitalRatingRepository.save(rating);
        
        updateHospitalAverageRating(rating.getHospital().getId());
        
        log.info("医院评分提交成功，医院ID: {}, 评分: {}", rating.getHospital().getId(), rating.getRating());
        return Result.success(saved);
    }

    /**
     * 更新医院平均评分
     */
    private void updateHospitalAverageRating(Long hospitalId) {
        List<HospitalRating> ratings = hospitalRatingRepository.findByHospitalIdAndDeletedFalse(hospitalId);
        
        if (ratings.isEmpty()) {
            return;
        }

        double avg = ratings.stream()
                .mapToInt(HospitalRating::getRating)
                .average()
                .orElse(0.0);

        Optional<PetHospital> hospitalOpt = petHospitalRepository.findById(hospitalId);
        if (hospitalOpt.isPresent()) {
            PetHospital hospital = hospitalOpt.get();
            hospital.setAverageRating(BigDecimal.valueOf(avg));
            hospital.setRatingCount(ratings.size());
            petHospitalRepository.save(hospital);
        }
    }

    /**
     * 获取医院的评分列表
     */
    public Result<List<HospitalRating>> getHospitalRatings(Long hospitalId) {
        List<HospitalRating> ratings = hospitalRatingRepository.findByHospitalIdAndDeletedFalseOrderByCreatedTimeDesc(hospitalId);
        return Result.success(ratings);
    }

    /**
     * 获取24小时急诊医院
     */
    public Result<List<PetHospital>> getEmergencyHospitals() {
        List<PetHospital> hospitals = petHospitalRepository.findByIsEmergencyTrueAndStatusAndDeletedFalse("ACTIVE");
        return Result.success(hospitals);
    }
}