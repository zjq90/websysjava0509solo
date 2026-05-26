package com.accounting.system.mapper;

import com.accounting.system.entity.TransferRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 转账记录Mapper接口
 */
@Mapper
public interface TransferRecordMapper extends BaseMapper<TransferRecord> {

    /**
     * 分页查询转账记录（带账户信息）
     *
     * @param page 分页对象
     * @return 转账记录列表
     */
    IPage<TransferRecord> selectPageWithDetail(Page<TransferRecord> page);
}
