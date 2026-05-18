package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 用户收藏实体类
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_favorite", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "heritage_id"})
})
public class UserFavorite extends BaseEntity {

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 文物ID
     */
    @Column(name = "heritage_id", nullable = false)
    private Long heritageId;
}