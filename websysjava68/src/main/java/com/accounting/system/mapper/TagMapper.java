package com.accounting.system.mapper;

import com.accounting.system.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 标签Mapper接口
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据交易记录ID查询关联的标签列表
     *
     * @param transactionId 交易记录ID
     * @return 标签列表
     */
    @Select("SELECT t.* FROM tag t INNER JOIN transaction_tag tt ON t.id = tt.tag_id " +
            "WHERE tt.transaction_id = #{transactionId} AND t.deleted = 0")
    List<Tag> selectTagsByTransactionId(@Param("transactionId") Long transactionId);
}
