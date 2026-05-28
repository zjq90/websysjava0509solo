package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_club_file")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "社团网盘文件")
public class ClubFile extends BaseEntity {

    @Schema(description = "社团ID", example = "1")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Schema(description = "文件名称", example = "社团章程.pdf")
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Schema(description = "存储文件名", example = "abc123.pdf")
    @Column(name = "file_name", length = 200)
    private String fileName;

    @Schema(description = "文件路径", example = "/uploads/club-files/1/abc123.pdf")
    @Column(name = "file_path", length = 500)
    private String filePath;

    @Schema(description = "文件类型：image/video/audio/word/excel/ppt/pdf/text/archive/other", example = "pdf")
    @Column(name = "file_type", length = 50)
    private String fileType;

    @Schema(description = "文件大小（字节）", example = "1024000")
    @Column(name = "file_size")
    private Long fileSize;

    @Schema(description = "文件夹", example = "活动资料")
    @Column(name = "folder", length = 100)
    private String folder;

    @Schema(description = "文件描述", example = "社团正式章程文件")
    @Column(name = "description", length = 500)
    private String description;

    @Schema(description = "上传人ID", example = "1")
    @Column(name = "upload_user_id")
    private Long uploadUserId;

    @Schema(description = "上传人姓名", example = "张三")
    @Column(name = "upload_username", length = 50)
    private String uploadUsername;

    @Schema(description = "上传时间", example = "2024-01-01 12:00:00")
    @Column(name = "upload_time", length = 50)
    private String uploadTime;

    @Schema(description = "下载次数", example = "50")
    @Column(name = "download_count", nullable = false)
    private Integer downloadCount = 0;

    @Schema(description = "文件分类：章程/策划模板/活动资料/其他", example = "章程")
    @Column(name = "category", length = 50)
    private String category;

    @Schema(description = "父目录ID", example = "0")
    @Column(name = "parent_id")
    private Long parentId = 0L;
}
