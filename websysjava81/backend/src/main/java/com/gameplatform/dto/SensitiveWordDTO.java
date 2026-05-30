package com.gameplatform.dto;

import lombok.Data;

@Data
public class SensitiveWordDTO {
    private String word;
    private Integer wordType;
    private String category;
}
