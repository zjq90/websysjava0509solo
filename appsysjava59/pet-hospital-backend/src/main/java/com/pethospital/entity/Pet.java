package com.pethospital.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String species;

    @Column(length = 50)
    private String breed;

    @Column(length = 10)
    private String gender;

    private Integer age;

    private Double weight;

    @Column(length = 20)
    private String color;

    @Column(length = 500)
    private String avatar;

    @Column(name = "allergy_history", length = 500)
    private String allergyHistory;

    @Column(length = 30)
    private String status;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
