package com.photostudio.service;

import com.photostudio.common.Result;
import com.photostudio.entity.Album;
import com.photostudio.entity.Photo;
import com.photostudio.repository.AlbumRepository;
import com.photostudio.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 云相册服务类
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PhotoRepository photoRepository;

    /**
     * 查询所有相册
     */
    public Result<List<Album>> findAll() {
        List<Album> list = albumRepository.findAll();
        return Result.success(list);
    }

    /**
     * 分页查询相册
     */
    public Result<Page<Album>> findPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Album> result = albumRepository.findAll(pageable);
        return Result.success(result);
    }

    /**
     * 根据ID查询相册
     */
    public Result<Album> findById(Long id) {
        Optional<Album> optional = albumRepository.findById(id);
        return optional.map(Result::success).orElse(Result.error("相册不存在"));
    }

    /**
     * 根据订单ID查询相册
     */
    public Result<Album> findByOrderId(Long orderId) {
        Optional<Album> optional = albumRepository.findByOrderId(orderId);
        return optional.map(Result::success).orElse(Result.error("相册不存在"));
    }

    /**
     * 根据客户ID查询相册
     */
    public Result<List<Album>> findByCustomerId(Long customerId) {
        List<Album> list = albumRepository.findByCustomerIdOrderByCreateTimeDesc(customerId);
        return Result.success(list);
    }

    /**
     * 创建相册
     */
    public Result<Album> create(Album album) {
        Album saved = albumRepository.save(album);
        return Result.success("相册创建成功", saved);
    }

    /**
     * 更新相册
     */
    public Result<Album> update(Album album) {
        if (album.getId() == null) {
            return Result.error("相册ID不能为空");
        }
        Album saved = albumRepository.save(album);
        return Result.success("相册更新成功", saved);
    }

    /**
     * 删除相册
     */
    public Result<Void> delete(Long id) {
        if (!albumRepository.existsById(id)) {
            return Result.error("相册不存在");
        }
        albumRepository.deleteById(id);
        return Result.success("相册删除成功", null);
    }

    /**
     * 获取相册照片列表
     */
    public Result<List<Photo>> getPhotos(Long albumId) {
        List<Photo> list = photoRepository.findByAlbumIdOrderBySortOrderAsc(albumId);
        return Result.success(list);
    }

    /**
     * 添加照片到相册
     */
    @Transactional
    public Result<Photo> addPhoto(Long albumId, Photo photo) {
        Optional<Album> albumOpt = albumRepository.findById(albumId);
        if (!albumOpt.isPresent()) {
            return Result.error("相册不存在");
        }
        
        photo.setAlbumId(albumId);
        Photo saved = photoRepository.save(photo);
        
        // 更新相册照片数量
        Album album = albumOpt.get();
        album.setTotalPhotos(photoRepository.countByAlbumId(albumId).intValue());
        albumRepository.save(album);
        
        return Result.success("照片添加成功", saved);
    }

    /**
     * 批量添加照片
     */
    @Transactional
    public Result<List<Photo>> addPhotos(Long albumId, List<Photo> photos) {
        Optional<Album> albumOpt = albumRepository.findById(albumId);
        if (!albumOpt.isPresent()) {
            return Result.error("相册不存在");
        }
        
        for (Photo photo : photos) {
            photo.setAlbumId(albumId);
            photoRepository.save(photo);
        }
        
        // 更新相册照片数量
        Album album = albumOpt.get();
        album.setTotalPhotos(photoRepository.countByAlbumId(albumId).intValue());
        albumRepository.save(album);
        
        return Result.success("照片批量添加成功", photos);
    }

    /**
     * 更新照片标记（喜欢/待修/删除）
     */
    @Transactional
    public Result<Photo> updatePhotoMark(Long photoId, String markType, Integer isSelected) {
        Optional<Photo> photoOpt = photoRepository.findById(photoId);
        if (!photoOpt.isPresent()) {
            return Result.error("照片不存在");
        }
        
        Photo photo = photoOpt.get();
        if (markType != null) {
            photo.setMarkType(markType);
        }
        if (isSelected != null) {
            photo.setIsSelected(isSelected);
        }
        
        Photo saved = photoRepository.save(photo);
        
        // 更新相册选中照片数量
        Long albumId = photo.getAlbumId();
        Optional<Album> albumOpt = albumRepository.findById(albumId);
        if (albumOpt.isPresent()) {
            Album album = albumOpt.get();
            album.setSelectedCount(photoRepository.countByAlbumIdAndIsSelected(albumId, 1).intValue());
            albumRepository.save(album);
        }
        
        return Result.success("照片标记更新成功", saved);
    }

    /**
     * 更新修图状态
     */
    @Transactional
    public Result<Photo> updateRetouchStatus(Long photoId, Integer retouchStatus, Long retoucherId, String remark) {
        Optional<Photo> photoOpt = photoRepository.findById(photoId);
        if (!photoOpt.isPresent()) {
            return Result.error("照片不存在");
        }
        
        Photo photo = photoOpt.get();
        photo.setRetouchStatus(retouchStatus);
        if (retoucherId != null) {
            photo.setRetoucherId(retoucherId);
        }
        if (remark != null) {
            photo.setRetouchRemark(remark);
        }
        
        if (retouchStatus == 1) {
            photo.setRetouchStartTime(java.time.LocalDateTime.now());
        } else if (retouchStatus >= 2) {
            photo.setRetouchEndTime(java.time.LocalDateTime.now());
        }
        
        Photo saved = photoRepository.save(photo);
        return Result.success("修图状态更新成功", saved);
    }

    /**
     * 获取统计信息
     */
    public Result<java.util.Map<String, Object>> getStatistics(Long albumId) {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalPhotos", photoRepository.countByAlbumId(albumId));
        stats.put("selectedCount", photoRepository.countByAlbumIdAndIsSelected(albumId, 1));
        
        // 各标记类型数量
        List<Photo> likePhotos = photoRepository.findByAlbumIdAndMarkTypeOrderBySortOrderAsc(albumId, "like");
        List<Photo> retouchPhotos = photoRepository.findByAlbumIdAndMarkTypeOrderBySortOrderAsc(albumId, "retouch");
        List<Photo> deletePhotos = photoRepository.findByAlbumIdAndMarkTypeOrderBySortOrderAsc(albumId, "delete");
        
        stats.put("likeCount", likePhotos.size());
        stats.put("retouchCount", retouchPhotos.size());
        stats.put("deleteCount", deletePhotos.size());
        
        return Result.success(stats);
    }
}
