package com.club.service;

import com.club.entity.ClubFile;
import com.club.entity.ClubMember;
import com.club.entity.User;
import com.club.entity.enums.MemberRoleEnum;
import com.club.exception.BusinessException;
import com.club.repository.ClubFileRepository;
import com.club.repository.ClubMemberRepository;
import com.club.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 社团网盘服务
 *
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
public class ClubFileService {

    @Autowired
    private ClubFileRepository clubFileRepository;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private UserRepository userRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/club-files/";

    /**
     * 上传文件
     */
    @Transactional(rollbackFor = Exception.class)
    public ClubFile uploadFile(Long clubId, String folder, String description, MultipartFile file, Long userId) throws IOException {
        log.info("上传文件 - clubId: {}, folder: {}, fileName: {}, userId: {}", clubId, folder, file.getOriginalFilename(), userId);

        if (!clubMemberRepository.existsByClubIdAndUserIdAndDeletedFalse(clubId, userId)) {
            throw new BusinessException("只有社团成员才能上传文件");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + fileExtension;
        long fileSize = file.getSize();
        String fileType = getFileType(fileExtension);

        File uploadDir = new File(UPLOAD_DIR + clubId + "/" + (folder != null ? folder : ""));
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        Path filePath = Paths.get(uploadDir.getPath(), newFileName);
        Files.copy(file.getInputStream(), filePath);

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        ClubFile clubFile = new ClubFile();
        clubFile.setClubId(clubId);
        clubFile.setName(originalFilename);
        clubFile.setFileName(newFileName);
        clubFile.setFilePath("/uploads/club-files/" + clubId + "/" + (folder != null ? folder + "/" : "") + newFileName);
        clubFile.setFileType(fileType);
        clubFile.setFileSize(fileSize);
        clubFile.setFolder(folder);
        clubFile.setDescription(description);
        clubFile.setUploadUserId(userId);
        clubFile.setUploadUsername(user.getUsername());
        clubFile.setUploadTime(LocalDateTime.now().format(FORMATTER));
        clubFile.setDownloadCount(0);

        ClubFile saved = clubFileRepository.save(clubFile);
        log.info("文件上传成功 - fileId: {}", saved.getId());
        return saved;
    }

    /**
     * 获取文件列表
     */
    public List<ClubFile> getFileList(Long clubId, String folder, String fileType) {
        log.info("获取文件列表 - clubId: {}, folder: {}, fileType: {}", clubId, folder, fileType);

        if (folder != null && !folder.isEmpty() && fileType != null && !fileType.isEmpty()) {
            return clubFileRepository.findByClubIdAndFolderAndFileTypeAndDeletedFalseOrderByCreateTimeDesc(clubId, folder, fileType);
        } else if (folder != null && !folder.isEmpty()) {
            return clubFileRepository.findByClubIdAndFolderAndDeletedFalseOrderByCreateTimeDesc(clubId, folder);
        } else if (fileType != null && !fileType.isEmpty()) {
            return clubFileRepository.findByClubIdAndFileTypeAndDeletedFalseOrderByCreateTimeDesc(clubId, fileType);
        } else {
            return clubFileRepository.findByClubIdAndDeletedFalseOrderByCreateTimeDesc(clubId);
        }
    }

    /**
     * 下载文件
     */
    @Transactional(rollbackFor = Exception.class)
    public ClubFile downloadFile(Long fileId) {
        log.info("下载文件 - fileId: {}", fileId);

        ClubFile file = clubFileRepository.findByIdAndDeletedFalse(fileId)
                .orElseThrow(() -> new BusinessException("文件不存在"));

        clubFileRepository.incrementDownloadCount(fileId);

        return file;
    }

    /**
     * 删除文件
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(Long fileId, Long userId) {
        log.info("删除文件 - fileId: {}, userId: {}", fileId, userId);

        ClubFile file = clubFileRepository.findByIdAndDeletedFalse(fileId)
                .orElseThrow(() -> new BusinessException("文件不存在"));

        ClubMember member = clubMemberRepository.findByClubIdAndUserIdAndDeletedFalse(file.getClubId(), userId).orElse(null);
        boolean isManager = member != null && (member.getRole() == MemberRoleEnum.PRESIDENT || member.getRole() == MemberRoleEnum.VICE_PRESIDENT);

        if (!file.getUploadUserId().equals(userId) && !isManager) {
            throw new BusinessException("只能删除自己上传的文件，或联系管理员删除");
        }

        file.setDeleted(true);
        clubFileRepository.save(file);

        File physicalFile = new File(UPLOAD_DIR + file.getClubId() + "/" + (file.getFolder() != null ? file.getFolder() + "/" : "") + file.getFileName());
        if (physicalFile.exists()) {
            physicalFile.delete();
        }

        log.info("文件删除成功 - fileId: {}", fileId);
    }

    /**
     * 获取文件类型
     */
    private String getFileType(String extension) {
        extension = extension.toLowerCase();
        if (".jpg|.jpeg|.png|.gif|.bmp|.webp".contains(extension)) {
            return "image";
        } else if (".mp4|.avi|.mov|.wmv|.mkv".contains(extension)) {
            return "video";
        } else if (".mp3|.wav|.flac|.aac".contains(extension)) {
            return "audio";
        } else if (".doc|.docx".contains(extension)) {
            return "word";
        } else if (".xls|.xlsx".contains(extension)) {
            return "excel";
        } else if (".ppt|.pptx".contains(extension)) {
            return "ppt";
        } else if (".pdf".equals(extension)) {
            return "pdf";
        } else if (".txt".equals(extension)) {
            return "text";
        } else if (".zip|.rar|.7z|.tar|.gz".contains(extension)) {
            return "archive";
        } else {
            return "other";
        }
    }
}
