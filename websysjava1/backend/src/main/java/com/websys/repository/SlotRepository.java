package com.websys.repository;

import com.websys.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 货道数据访问接口
 * 提供货道的增删改查操作
 */
@Repository
public interface SlotRepository extends JpaRepository<Slot, Long>, JpaSpecificationExecutor<Slot> {

    /**
     * 根据设备ID查找所有货道
     * @param deviceId 设备ID
     * @return 货道列表
     */
    List<Slot> findByDeviceIdOrderBySlotNo(Long deviceId);

    /**
     * 根据设备编号查找所有货道
     * @param deviceCode 设备编号
     * @return 货道列表
     */
    List<Slot> findByDeviceCodeOrderBySlotNo(String deviceCode);

    /**
     * 根据设备ID和货道编号查找货道
     * @param deviceId 设备ID
     * @param slotNo 货道编号
     * @return 货道对象
     */
    Optional<Slot> findByDeviceIdAndSlotNo(Long deviceId, String slotNo);

    /**
     * 删除指定设备的所有货道
     * @param deviceId 设备ID
     */
    void deleteByDeviceId(Long deviceId);

    /**
     * 统计设备的缺货货道数
     * @param deviceId 设备ID
     * @param status 状态（缺货=3）
     * @return 数量
     */
    long countByDeviceIdAndStatus(Long deviceId, Integer status);
}
