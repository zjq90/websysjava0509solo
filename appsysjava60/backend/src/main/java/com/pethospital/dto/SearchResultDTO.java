package com.pethospital.dto;

import lombok.Data;

/**
 * 搜索结果DTO
 * 封装知识库搜索结果，包含高亮关键词和相关性分数
 * 
 * @author Pet Hospital Team
 */
@Data
public class SearchResultDTO {
    
    /**
     * 结果ID
     */
    private Long id;
    
    /**
     * 结果类型：disease-疾病，medicine-药品，case-案例
     */
    private String type;
    
    /**
     * 标题（高亮后）
     */
    private String title;
    
    /**
     * 摘要内容（高亮后）
     */
    private String summary;
    
    /**
     * 相关性分数（越高越相关）
     */
    private Double relevanceScore;
    
    /**
     * 宠物类型
     */
    private String petType;
}
