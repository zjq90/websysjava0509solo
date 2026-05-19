package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "heritage")
public class Heritage extends BaseEntity {

    @Column(name = "heritage_code", unique = true, nullable = false, length = 50)
    private String heritageCode;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "english_name", length = 200)
    private String englishName;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "sub_category", length = 100)
    private String subCategory;

    @Column(name = "dynasty", length = 50)
    private String dynasty;

    @Column(name = "period", length = 100)
    private String period;

    @Column(name = "material", length = 100)
    private String material;

    @Column(name = "technique", length = 200)
    private String technique;

    @Column(name = "size", length = 200)
    private String size;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "origin", length = 200)
    private String origin;

    @Column(name = "discovery_place", length = 200)
    private String discoveryPlace;

    @Column(name = "discovery_year")
    private Integer discoveryYear;

    @Column(name = "current_location", length = 200)
    private String currentLocation;

    @Column(name = "owner", length = 200)
    private String owner;

    @Column(name = "condition_status", length = 50)
    private String conditionStatus;

    @Column(name = "level", length = 50)
    private String level;

    @Column(name = "estimated_value")
    private BigDecimal estimatedValue;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "historical_background", columnDefinition = "TEXT")
    private String historicalBackground;

    @Column(name = "provenance", columnDefinition = "TEXT")
    private String provenance;

    @Column(name = "image_urls", columnDefinition = "TEXT")
    private String imageUrls;

    @Column(name = "data_source", length = 100)
    private String dataSource;

    @Column(name = "source_url", length = 500)
    private String sourceUrl;

    @Column(name = "is_master_data", nullable = false)
    private Boolean isMasterData = false;

    @Column(name = "master_code", length = 50)
    private String masterCode;

    @Column(name = "quality_score")
    private Integer qualityScore;

    @Column(name = "verification_status", length = 50)
    private String verificationStatus;

    @Column(name = "data_type", length = 50)
    private String dataType;
}
