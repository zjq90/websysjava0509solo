package com.bikesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bikesystem.entity.PaymentRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付记录Mapper接口
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Mapper
public interface PaymentRecordMapper extends BaseMapper<PaymentRecord> {
}
