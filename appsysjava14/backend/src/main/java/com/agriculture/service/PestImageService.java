package com.agriculture.service;

import com.agriculture.entity.PestImage;
import com.agriculture.repository.PestImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 病虫害图片服务类
 * 提供病虫害图片管理的业务逻辑
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Service
@Transactional
public class PestImageService {

    @Autowired
    private PestImageRepository pestImageRepository;

    /**
     * 保存图片信息
     * 
     * @param pestImage 图片信息
     * @return 保存后的图片
     */
    public PestImage save(PestImage pestImage) {
        return pestImageRepository.save(pestImage);
    }

    /**
     * 批量保存图片
     * 
     * @param images 图片列表
     * @return 保存后的图片列表
     */
    public List<PestImage> saveAll(List<PestImage> images) {
        return pestImageRepository.saveAll(images);
    }

    /**
     * 更新图片信息
     * 
     * @param pestImage 图片信息
     * @return 更新后的图片
     */
    public PestImage update(PestImage pestImage) {
        if (!pestImageRepository.existsById(pestImage.getId())) {
            throw new RuntimeException("图片不存在");
        }
        return pestImageRepository.save(pestImage);
    }

    /**
     * 逻辑删除图片（修改状态）
     * 
     * @param id 图片ID
     */
    public void deleteById(Long id) {
        Optional<PestImage> imageOpt = pestImageRepository.findById(id);
        if (imageOpt.isPresent()) {
            PestImage image = imageOpt.get();
            image.setStatus("DELETED");
            pestImageRepository.save(image);
        }
    }

    /**
     * 物理删除图片
     * 
     * @param id 图片ID
     */
    public void physicalDeleteById(Long id) {
        pestImageRepository.deleteById(id);
    }

    /**
     * 根据ID查找图片
     * 
     * @param id 图片ID
     * @return 图片对象
     */
    public Optional<PestImage> findById(Long id) {
        return pestImageRepository.findById(id);
    }

    /**
     * 根据田间记录ID查询图片
     * 
     * @param fieldRecordId 田间记录ID
     * @return 图片列表
     */
    public List<PestImage> findByFieldRecordId(Long fieldRecordId) {
        return pestImageRepository.findByFieldRecordIdAndStatusOrderByCreatedAtAsc(fieldRecordId, "ACTIVE");
    }

    /**
     * 根据图片类型查询
     * 
     * @param imageType 图片类型
     * @return 图片列表
     */
    public List<PestImage> findByImageType(String imageType) {
        return pestImageRepository.findByImageTypeAndStatusOrderByCreatedAtDesc(imageType, "ACTIVE");
    }

    /**
     * 统计田间记录的图片数量
     * 
     * @param fieldRecordId 田间记录ID
     * @return 图片数量
     */
    public long countByFieldRecordId(Long fieldRecordId) {
        return pestImageRepository.countByFieldRecordIdAndStatus(fieldRecordId, "ACTIVE");
    }
}
