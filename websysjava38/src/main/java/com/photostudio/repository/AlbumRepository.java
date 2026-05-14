package com.photostudio.repository;

import com.photostudio.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 云相册数据访问接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface AlbumRepository extends JpaRepository<Album, Long>, JpaSpecificationExecutor<Album> {

    /**
     * 根据相册编号查询
     */
    Optional<Album> findByAlbumNo(String albumNo);

    /**
     * 根据订单ID查询
     */
    Optional<Album> findByOrderId(Long orderId);

    /**
     * 根据客户ID查询
     */
    List<Album> findByCustomerIdOrderByCreateTimeDesc(Long customerId);

    /**
     * 根据状态查询
     */
    List<Album> findByStatusOrderByCreateTimeDesc(Integer status);
}
