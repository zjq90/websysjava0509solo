package com.vending.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类实体类
 */
@Data
@Entity
@Table(name = "category")
@Schema(description = "商品分类")
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "分类ID")
    private Long id;
    
    @Column(nullable = false, unique = true)
    @Schema(description = "分类名称", required = true)
    private String name;
    
    @Schema(description = "分类描述")
    private String description;
    
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();
}
