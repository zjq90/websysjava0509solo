package com.petclinic.dto;

import lombok.Data;

/**
 * 疾病热力图DTO
 */
@Data
public class DiseaseHeatmapDTO {

    /**
     * 疾病名称
     */
    private String diseaseName;

    /**
     * 宠物类型
     */
    private String petType;

    /**
     * 宠物年龄
     */
    private Integer petAge;

    /**
     * 病例数量
     */
    private Long count;
}
