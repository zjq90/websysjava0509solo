package com.platform.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.management.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单Mapper接口
 * 
 * @author platform
 * @version 1.0.0
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
