package com.psyconsult.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "counselors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Counselor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(length = 50)
    private String name;

    @Column(length = 500)
    private String avatar;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(length = 500)
    private String qualification;

    @Column(length = 2000)
    private String introduction;

    @ElementCollection
    @CollectionTable(name = "counselor_specialties", joinColumns = @JoinColumn(name = "counselor_id"))
    @Column(name = "specialty")
    private List<String> specialties;

    @ElementCollection
    @CollectionTable(name = "counselor_languages", joinColumns = @JoinColumn(name = "counselor_id"))
    @Column(name = "language")
    private List<String> languages;

    @Column(precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "rating", precision = 3, scale = 1)
    private BigDecimal rating = BigDecimal.ZERO;

    @Column(name = "total_consultations")
    private Integer totalConsultations = 0;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(length = 20)
    private String role = "COUNSELOR";

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(nullable = false)
    private Boolean enabled = true;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
