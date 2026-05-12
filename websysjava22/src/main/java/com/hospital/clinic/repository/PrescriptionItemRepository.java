package com.hospital.clinic.repository;

import com.hospital.clinic.entity.Prescription;
import com.hospital.clinic.entity.PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处方明细Repository
 * 处方明细数据访问接口
 */
@Repository
public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem, Long> {

    /**
     * 根据处方查询明细
     */
    List<PrescriptionItem> findByPrescription(Prescription prescription);
}
