package com.accounting.system.service;

import com.accounting.system.dto.TagDTO;
import com.accounting.system.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 标签Service接口
 */
public interface TagService extends IService<Tag> {

    /**
     * 查询所有标签列表
     *
     * @return 标签列表
     */
    List<Tag> listAll();

    /**
     * 根据ID获取标签详情
     *
     * @param id 标签ID
     * @return 标签详情
     */
    Tag getDetailById(Long id);

    /**
     * 新增标签
     *
     * @param dto 标签请求DTO
     * @return 新增的标签
     */
    Tag addTag(TagDTO dto);

    /**
     * 更新标签
     *
     * @param dto 标签请求DTO
     * @return 更新后的标签
     */
    Tag updateTag(TagDTO dto);

    /**
     * 删除标签
     *
     * @param id 标签ID
     */
    void deleteTag(Long id);

    /**
     * 根据交易记录ID查询关联的标签列表
     *
     * @param transactionId 交易记录ID
     * @return 标签列表
     */
    List<Tag> listByTransactionId(Long transactionId);
}
