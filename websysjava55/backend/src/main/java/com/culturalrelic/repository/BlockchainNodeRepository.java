package com.culturalrelic.repository;

import com.culturalrelic.entity.BlockchainNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 区块链节点数据访问层
 */
@Repository
public interface BlockchainNodeRepository extends JpaRepository<BlockchainNode, Long>, JpaSpecificationExecutor<BlockchainNode> {

    /**
     * 根据节点名称查询
     */
    BlockchainNode findByNodeName(String nodeName);

    /**
     * 根据可用区查询
     */
    List<BlockchainNode> findByAvailabilityZone(String availabilityZone);

    /**
     * 根据状态查询
     */
    List<BlockchainNode> findByStatus(Integer status);

    /**
     * 查询启用的节点
     */
    List<BlockchainNode> findByEnabled(Integer enabled);

    /**
     * 逻辑删除
     */
    @Modifying
    @Query("UPDATE BlockchainNode n SET n.deleted = 1 WHERE n.id = :id")
    int logicDelete(@Param("id") Long id);
}
