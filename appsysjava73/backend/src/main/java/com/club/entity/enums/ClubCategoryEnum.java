package com.club.entity.enums;

import lombok.Getter;

/**
 * 社团分类枚举
 *
 * @author club-management
 * @version 1.0.0
 */
@Getter
public enum ClubCategoryEnum {

    ACADEMIC_TECHNOLOGY("学术科技", "学术科技类社团", "#4A90E2"),
    CULTURE_ART("文化艺术", "文化艺术类社团", "#E24A68"),
    SPORTS("体育竞技", "体育竞技类社团", "#50C878"),
    PUBLIC_WELFARE("公益实践", "公益实践类社团", "#FFD700"),
    INNOVATION("创新创业", "创新创业类社团", "#9B59B6"),
    OTHER("其他", "其他类型社团", "#95A5A6");

    private final String name;
    private final String description;
    private final String color;

    ClubCategoryEnum(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public static java.util.List<java.util.Map<String, String>> getCategoryList() {
        return java.util.Arrays.stream(values())
                .map(category -> {
                    java.util.Map<String, String> map = new java.util.LinkedHashMap<>();
                    map.put("code", category.name());
                    map.put("name", category.getName());
                    map.put("description", category.getDescription());
                    map.put("color", category.getColor());
                    return map;
                })
                .collect(java.util.stream.Collectors.toList());
    }
}
