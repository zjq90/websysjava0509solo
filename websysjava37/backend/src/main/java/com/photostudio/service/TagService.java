package com.photostudio.service;

import com.photostudio.entity.Customer;
import com.photostudio.entity.Tag;
import com.photostudio.repository.CustomerRepository;
import com.photostudio.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 标签服务层
 * 处理标签相关的业务逻辑
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
@Transactional
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * 创建标签
     */
    public Tag createTag(Tag tag) {
        if (tagRepository.existsByNameAndDeletedFalse(tag.getName())) {
            throw new RuntimeException("标签名称已存在");
        }
        return tagRepository.save(tag);
    }

    /**
     * 更新标签
     */
    public Tag updateTag(Long id, Tag tagDetails) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("标签不存在"));
        
        if (!tag.getName().equals(tagDetails.getName()) &&
            tagRepository.existsByNameAndDeletedFalse(tagDetails.getName())) {
            throw new RuntimeException("标签名称已存在");
        }

        tag.setName(tagDetails.getName());
        tag.setDescription(tagDetails.getDescription());
        tag.setAutoTag(tagDetails.getAutoTag());

        return tagRepository.save(tag);
    }

    /**
     * 删除标签（软删除）
     */
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("标签不存在"));
        tag.setDeleted(true);
        tagRepository.save(tag);
    }

    /**
     * 根据ID查询标签
     */
    @Transactional(readOnly = true)
    public Optional<Tag> getTagById(Long id) {
        return tagRepository.findById(id).filter(t -> !t.getDeleted());
    }

    /**
     * 查询所有标签
     */
    @Transactional(readOnly = true)
    public List<Tag> getAllTags() {
        return tagRepository.findAllByDeletedFalse();
    }

    /**
     * 为客户添加标签
     */
    public Customer addTagToCustomer(Long customerId, Long tagId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("客户不存在"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("标签不存在"));

        if (!customer.getTags().contains(tag)) {
            customer.getTags().add(tag);
            customerRepository.save(customer);
        }
        return customer;
    }

    /**
     * 移除客户标签
     */
    public Customer removeTagFromCustomer(Long customerId, Long tagId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("客户不存在"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("标签不存在"));

        customer.getTags().remove(tag);
        return customerRepository.save(customer);
    }

    /**
     * 根据名称查询标签
     */
    @Transactional(readOnly = true)
    public Optional<Tag> getTagByName(String name) {
        return tagRepository.findByNameAndDeletedFalse(name);
    }
}
