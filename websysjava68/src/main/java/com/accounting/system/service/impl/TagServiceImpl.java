package com.accounting.system.service.impl;

import com.accounting.system.common.BusinessException;
import com.accounting.system.common.ResultCode;
import com.accounting.system.dto.TagDTO;
import com.accounting.system.entity.Tag;
import com.accounting.system.mapper.TagMapper;
import com.accounting.system.service.TagService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 标签Service实现类
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
        implements TagService {

    @Override
    public List<Tag> listAll() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Tag::getSortOrder);
        return list(wrapper);
    }

    @Override
    public Tag getDetailById(Long id) {
        Tag tag = getById(id);
        if (tag == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        return tag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Tag addTag(TagDTO dto) {
        // 检查标签名称是否重复
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getTagName, dto.getTagName());
        if (count(wrapper) > 0) {
            throw new BusinessException("标签名称已存在");
        }

        Tag tag = new Tag();
        BeanUtils.copyProperties(dto, tag);
        if (tag.getTagType() == null) {
            tag.setTagType("CUSTOM");
        }
        save(tag);
        return tag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Tag updateTag(TagDTO dto) {
        Tag existTag = getById(dto.getId());
        if (existTag == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }

        // 检查标签名称是否与其他标签重复
        if (!existTag.getTagName().equals(dto.getTagName())) {
            LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Tag::getTagName, dto.getTagName());
            if (count(wrapper) > 0) {
                throw new BusinessException("标签名称已存在");
            }
        }

        BeanUtils.copyProperties(dto, existTag);
        updateById(existTag);
        return existTag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(Long id) {
        Tag existTag = getById(id);
        if (existTag == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        removeById(id);
    }

    @Override
    public List<Tag> listByTransactionId(Long transactionId) {
        return baseMapper.selectTagsByTransactionId(transactionId);
    }
}
