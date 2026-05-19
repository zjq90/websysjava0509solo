package com.petclinic.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 饮食建议实体类
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "diet_suggestions")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DietSuggestion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "calorie_recommendation")
    private Integer calorieRecommendation;

    @Column(name = "food_type", length = 50)
    private String foodType;

    @Column(name = "feeding_frequency", length = 50)
    private String feedingFrequency;

    @Column(name = "reference_source", length = 200)
    private String referenceSource;

    @Column(name = "reference_url", length = 255)
    private String referenceUrl;

    @Column(name = "generated_time", nullable = false)
    private LocalDateTime generatedTime;
}