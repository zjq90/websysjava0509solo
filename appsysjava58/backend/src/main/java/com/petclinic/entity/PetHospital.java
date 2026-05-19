package com.petclinic.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 宠物医院实体类
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "pet_hospitals")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PetHospital extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "province", length = 50)
    private String province;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "district", length = 50)
    private String district;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "latitude", precision = 10, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 6)
    private BigDecimal longitude;

    @Column(name = "business_hours", length = 200)
    private String businessHours;

    @Column(name = "services", length = 500)
    private String services;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "rating_count", nullable = false)
    private Integer ratingCount = 0;

    @Column(name = "logo", length = 255)
    private String logo;

    @Column(name = "images", length = 1000)
    private String images;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "is_24h", nullable = false)
    private Boolean is24h = false;

    @Column(name = "is_emergency", nullable = false)
    private Boolean isEmergency = false;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";
}