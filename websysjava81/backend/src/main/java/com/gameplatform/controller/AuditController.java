package com.gameplatform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplatform.common.Result;
import com.gameplatform.dto.CommentAuditDTO;
import com.gameplatform.dto.ReportHandleDTO;
import com.gameplatform.dto.SensitiveWordDTO;
import com.gameplatform.entity.SensitiveWord;
import com.gameplatform.service.AuditService;
import com.gameplatform.vo.CommentVO;
import com.gameplatform.vo.ReportStatisticsVO;
import com.gameplatform.vo.ReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @GetMapping("/comments")
    public Result<Page<CommentVO>> getCommentList(
            @RequestParam(required = false) Integer auditStatus,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(auditService.getCommentList(auditStatus, keyword, pageNum, pageSize));
    }

    @PostMapping("/comment/audit")
    public Result<Void> auditComment(@RequestBody CommentAuditDTO dto) {
        auditService.auditComment(dto);
        return Result.success();
    }

    @GetMapping("/sensitive-words")
    public Result<Page<SensitiveWord>> getSensitiveWordList(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(auditService.getSensitiveWordList(keyword, pageNum, pageSize));
    }

    @PostMapping("/sensitive-word")
    public Result<Void> addSensitiveWord(@RequestBody SensitiveWordDTO dto) {
        auditService.addSensitiveWord(dto);
        return Result.success();
    }

    @PutMapping("/sensitive-word/{id}")
    public Result<Void> updateSensitiveWord(@PathVariable Long id, @RequestBody SensitiveWordDTO dto) {
        auditService.updateSensitiveWord(id, dto);
        return Result.success();
    }

    @DeleteMapping("/sensitive-word/{id}")
    public Result<Void> deleteSensitiveWord(@PathVariable Long id) {
        auditService.deleteSensitiveWord(id);
        return Result.success();
    }

    @GetMapping("/sensitive-word/export")
    public void exportSensitiveWords(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=sensitive_words.txt");
        
        List<SensitiveWord> words = auditService.getAllSensitiveWords();
        PrintWriter writer = response.getWriter();
        for (SensitiveWord word : words) {
            writer.println(word.getWord() + "," + word.getWordType() + "," + word.getCategory());
        }
        writer.flush();
    }

    @PostMapping("/sensitive-word/import")
    public Result<Void> importSensitiveWords(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 1) {
                SensitiveWordDTO dto = new SensitiveWordDTO();
                dto.setWord(parts[0].trim());
                dto.setWordType(parts.length > 1 ? Integer.parseInt(parts[1].trim()) : 0);
                dto.setCategory(parts.length > 2 ? parts[2].trim() : "");
                try {
                    auditService.addSensitiveWord(dto);
                } catch (Exception e) {
                    // 忽略重复的词
                }
            }
        }
        reader.close();
        return Result.success();
    }

    @GetMapping("/reports")
    public Result<Page<ReportVO>> getReportList(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String reasonType,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(auditService.getReportList(status, reasonType, pageNum, pageSize));
    }

    @PostMapping("/report/handle")
    public Result<Void> handleReport(@RequestBody ReportHandleDTO dto) {
        auditService.handleReport(dto);
        return Result.success();
    }

    @GetMapping("/report/statistics")
    public Result<ReportStatisticsVO> getReportStatistics() {
        return Result.success(auditService.getReportStatistics());
    }
}
