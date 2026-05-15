package com.teaching.service;

import com.teaching.entity.CourseMaterial;
import com.teaching.entity.CourseVideo;
import com.teaching.entity.User;
import com.teaching.repository.CourseMaterialRepository;
import com.teaching.repository.CourseVideoRepository;
import com.teaching.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private CourseMaterialRepository courseMaterialRepository;

    @Autowired
    private CourseVideoRepository courseVideoRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload-path:./uploads}")
    private String uploadPath;

    @Transactional
    public CourseMaterial uploadMaterial(Long courseId, String title, String description,
                                         MultipartFile file, Long uploaderId) throws IOException {
        User uploader = userRepository.findById(uploaderId).orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Path materialDir = Paths.get(uploadPath, "materials").toAbsolutePath().normalize();
        Files.createDirectories(materialDir);
        
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String newFilename = UUID.randomUUID().toString() + fileExtension;
        
        Path targetLocation = materialDir.resolve(newFilename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        CourseMaterial material = new CourseMaterial();
        material.setCourseId(courseId);
        material.setTitle(title);
        material.setDescription(description);
        material.setFileName(originalFilename);
        material.setFilePath(targetLocation.toString());
        material.setFileSize(file.getSize());
        material.setFileType(file.getContentType());
        material.setUploaderId(uploaderId);
        material.setUploaderName(uploader.getRealName() != null ? uploader.getRealName() : uploader.getUsername());
        material.setDownloadCount(0);
        material.setStatus(1);
        
        return courseMaterialRepository.save(material);
    }

    public List<CourseMaterial> getCourseMaterials(Long courseId) {
        return courseMaterialRepository.findByCourseIdOrderByCreateTimeDesc(courseId);
    }

    public Resource downloadMaterial(Long materialId) throws MalformedURLException {
        CourseMaterial material = courseMaterialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("资料不存在"));
        
        material.setDownloadCount(material.getDownloadCount() + 1);
        courseMaterialRepository.save(material);
        
        Path filePath = Paths.get(material.getFilePath()).normalize();
        return new UrlResource(filePath.toUri());
    }

    @Transactional
    public CourseVideo uploadVideo(Long courseId, String title, String description,
                                MultipartFile file, Long uploaderId) throws IOException {
        User uploader = userRepository.findById(uploaderId).orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Path videoDir = Paths.get(uploadPath, "videos").toAbsolutePath().normalize();
        Files.createDirectories(videoDir);
        
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String newFilename = UUID.randomUUID().toString() + fileExtension;
        
        Path targetLocation = videoDir.resolve(newFilename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        CourseVideo video = new CourseVideo();
        video.setCourseId(courseId);
        video.setTitle(title);
        video.setDescription(description);
        video.setFileName(originalFilename);
        video.setFilePath(targetLocation.toString());
        video.setFileSize(file.getSize());
        video.setUploaderId(uploaderId);
        video.setUploaderName(uploader.getRealName() != null ? uploader.getRealName() : uploader.getUsername());
        video.setViewCount(0);
        video.setStatus(1);
        
        return courseVideoRepository.save(video);
    }

    public List<CourseVideo> getCourseVideos(Long courseId) {
        return courseVideoRepository.findByCourseIdOrderByCreateTimeDesc(courseId);
    }

    public Resource getVideoResource(Long videoId) throws MalformedURLException {
        CourseVideo video = courseVideoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("视频不存在"));
        
        video.setViewCount(video.getViewCount() + 1);
        courseVideoRepository.save(video);
        
        Path filePath = Paths.get(video.getFilePath()).normalize();
        return new UrlResource(filePath.toUri());
    }

    @Transactional
    public void deleteMaterial(Long materialId) {
        CourseMaterial material = courseMaterialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("资料不存在"));
        try {
            Files.deleteIfExists(Paths.get(material.getFilePath()));
        } catch (IOException e) {
        }
        courseMaterialRepository.delete(material);
    }

    @Transactional
    public void deleteVideo(Long videoId) {
        CourseVideo video = courseVideoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("视频不存在"));
        try {
            Files.deleteIfExists(Paths.get(video.getFilePath()));
        } catch (IOException e) {
        }
        courseVideoRepository.delete(video);
    }
}
