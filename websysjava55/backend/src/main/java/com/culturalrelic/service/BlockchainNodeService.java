package com.culturalrelic.service;

import com.culturalrelic.entity.BlockchainNode;
import com.culturalrelic.repository.BlockchainNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 区块链节点业务逻辑层
 */
@Service
public class BlockchainNodeService {

    @Autowired
    private BlockchainNodeRepository blockchainNodeRepository;

    /**
     * 新增节点
     */
    public BlockchainNode save(BlockchainNode node) {
        return blockchainNodeRepository.save(node);
    }

    /**
     * 根据ID查询
     */
    public Optional<BlockchainNode> findById(Long id) {
        return blockchainNodeRepository.findById(id);
    }

    /**
     * 查询所有（分页）
     */
    public Page<BlockchainNode> findAll(Pageable pageable) {
        return blockchainNodeRepository.findAll(pageable);
    }

    /**
     * 查询所有
     */
    public List<BlockchainNode> findAll() {
        return blockchainNodeRepository.findAll();
    }

    /**
     * 更新节点
     */
    public BlockchainNode update(BlockchainNode node) {
        return blockchainNodeRepository.save(node);
    }

    /**
     * 删除节点
     */
    @Transactional
    public boolean delete(Long id) {
        int rows = blockchainNodeRepository.logicDelete(id);
        return rows > 0;
    }

    /**
     * 根据节点名称查询
     */
    public BlockchainNode findByNodeName(String nodeName) {
        return blockchainNodeRepository.findByNodeName(nodeName);
    }

    /**
     * 根据可用区查询
     */
    public List<BlockchainNode> findByAvailabilityZone(String zone) {
        return blockchainNodeRepository.findByAvailabilityZone(zone);
    }

    /**
     * 根据状态查询
     */
    public List<BlockchainNode> findByStatus(Integer status) {
        return blockchainNodeRepository.findByStatus(status);
    }

    /**
     * 查询启用的节点
     */
    public List<BlockchainNode> findByEnabled(Integer enabled) {
        return blockchainNodeRepository.findByEnabled(enabled);
    }
}
