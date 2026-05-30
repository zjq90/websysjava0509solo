package com.gameplatform.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplatform.dto.CommentAuditDTO;
import com.gameplatform.dto.ReportHandleDTO;
import com.gameplatform.dto.SensitiveWordDTO;
import com.gameplatform.entity.*;
import com.gameplatform.mapper.*;
import com.gameplatform.vo.CommentVO;
import com.gameplatform.vo.ReportStatisticsVO;
import com.gameplatform.vo.ReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AuditService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GameMapper gameMapper;

    private List<SensitiveWord> sensitiveWordsCache;
    private LocalDateTime cacheTime;
    private static final long CACHE_DURATION = 5;

    public Page<CommentVO> getCommentList(Integer auditStatus, String keyword, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        
        if (auditStatus != null) {
            wrapper.eq(Comment::getAuditStatus, auditStatus);
        }
        
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(Comment::getContent, keyword);
        }
        
        wrapper.orderByDesc(Comment::getCreateTime);
        
        Page<Comment> page = new Page<>(pageNum, pageSize);
        Page<Comment> commentPage = commentMapper.selectPage(page, wrapper);
        
        List<CommentVO> voList = commentPage.getRecords().stream().map(this::convertCommentToVO)
                .collect(Collectors.toList());
        
        Page<CommentVO> result = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        result.setRecords(voList);
        return result;
    }

    @Transactional
    public void auditComment(CommentAuditDTO dto) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getCommentId, dto.getCommentId());
        Comment comment = new Comment();
        comment.setAuditStatus(dto.getAuditStatus());
        comment.setAuditTime(LocalDateTime.now());
        comment.setAuditorId(dto.getAuditorId());
        commentMapper.update(comment, wrapper);
    }

    public boolean containsSensitiveWord(String content) {
        refreshSensitiveWordsCache();
        if (sensitiveWordsCache == null || sensitiveWordsCache.isEmpty()) {
            return false;
        }
        
        for (SensitiveWord word : sensitiveWordsCache) {
            if (word.getWordType() == 1) {
                Pattern pattern = Pattern.compile(word.getWord());
                if (pattern.matcher(content).find()) {
                    return true;
                }
            } else {
                if (content.contains(word.getWord())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void refreshSensitiveWordsCache() {
        if (sensitiveWordsCache == null || cacheTime == null 
                || Duration.between(cacheTime, LocalDateTime.now()).toMinutes() > CACHE_DURATION) {
            LambdaQueryWrapper<SensitiveWord> wrapper = new LambdaQueryWrapper<>();
            sensitiveWordsCache = sensitiveWordMapper.selectList(wrapper);
            cacheTime = LocalDateTime.now();
        }
    }

    public Page<SensitiveWord> getSensitiveWordList(String keyword, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SensitiveWord> wrapper = new LambdaQueryWrapper<>();
        
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(SensitiveWord::getWord, keyword);
        }
        
        wrapper.orderByDesc(SensitiveWord::getCreateTime);
        
        Page<SensitiveWord> page = new Page<>(pageNum, pageSize);
        return sensitiveWordMapper.selectPage(page, wrapper);
    }

    @Transactional
    public void addSensitiveWord(SensitiveWordDTO dto) {
        SensitiveWord word = new SensitiveWord();
        word.setWord(dto.getWord());
        word.setWordType(dto.getWordType());
        word.setCategory(dto.getCategory());
        word.setCreateTime(LocalDateTime.now());
        sensitiveWordMapper.insert(word);
        sensitiveWordsCache = null;
    }

    @Transactional
    public void updateSensitiveWord(Long id, SensitiveWordDTO dto) {
        SensitiveWord word = new SensitiveWord();
        word.setId(id);
        word.setWord(dto.getWord());
        word.setWordType(dto.getWordType());
        word.setCategory(dto.getCategory());
        sensitiveWordMapper.updateById(word);
        sensitiveWordsCache = null;
    }

    @Transactional
    public void deleteSensitiveWord(Long id) {
        sensitiveWordMapper.deleteById(id);
        sensitiveWordsCache = null;
    }

    public List<SensitiveWord> getAllSensitiveWords() {
        LambdaQueryWrapper<SensitiveWord> wrapper = new LambdaQueryWrapper<>();
        return sensitiveWordMapper.selectList(wrapper);
    }

    public Page<ReportVO> getReportList(Integer status, String reasonType, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
        
        if (status != null) {
            wrapper.eq(Report::getStatus, status);
        }
        
        if (StrUtil.isNotBlank(reasonType)) {
            wrapper.eq(Report::getReasonType, reasonType);
        }
        
        wrapper.orderByDesc(Report::getCreateTime);
        
        Page<Report> page = new Page<>(pageNum, pageSize);
        Page<Report> reportPage = reportMapper.selectPage(page, wrapper);
        
        List<ReportVO> voList = reportPage.getRecords().stream().map(this::convertReportToVO)
                .collect(Collectors.toList());
        
        Page<ReportVO> result = new Page<>(reportPage.getCurrent(), reportPage.getSize(), reportPage.getTotal());
        result.setRecords(voList);
        return result;
    }

    @Transactional
    public void handleReport(ReportHandleDTO dto) {
        LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Report::getReportId, dto.getReportId());
        Report report = new Report();
        report.setStatus(dto.getStatus());
        report.setHandleTime(LocalDateTime.now());
        report.setHandlerId(dto.getHandlerId());
        report.setHandlerName(dto.getHandlerName());
        reportMapper.update(report, wrapper);
    }

    public ReportStatisticsVO getReportStatistics() {
        ReportStatisticsVO vo = new ReportStatisticsVO();
        
        Map<String, Long> typeDistribution = new HashMap<>();
        String[] types = {"porn", "violence", "advertise", "other"};
        for (String type : types) {
            LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Report::getReasonType, type);
            typeDistribution.put(type, reportMapper.selectCount(wrapper));
        }
        vo.setTypeDistribution(typeDistribution);
        
        Map<String, Long> statusDistribution = new HashMap<>();
        LambdaQueryWrapper<Report> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(Report::getStatus, 0);
        statusDistribution.put("pending", reportMapper.selectCount(pendingWrapper));
        
        LambdaQueryWrapper<Report> confirmedWrapper = new LambdaQueryWrapper<>();
        confirmedWrapper.eq(Report::getStatus, 1);
        statusDistribution.put("confirmed", reportMapper.selectCount(confirmedWrapper));
        
        LambdaQueryWrapper<Report> rejectedWrapper = new LambdaQueryWrapper<>();
        rejectedWrapper.eq(Report::getStatus, 2);
        statusDistribution.put("rejected", reportMapper.selectCount(rejectedWrapper));
        vo.setStatusDistribution(statusDistribution);
        
        LambdaQueryWrapper<Report> handledWrapper = new LambdaQueryWrapper<>();
        handledWrapper.isNotNull(Report::getHandleTime);
        List<Report> handledReports = reportMapper.selectList(handledWrapper);
        
        if (!handledReports.isEmpty()) {
            double totalMinutes = 0;
            for (Report report : handledReports) {
                if (report.getCreateTime() != null && report.getHandleTime() != null) {
                    totalMinutes += Duration.between(report.getCreateTime(), report.getHandleTime()).toMinutes();
                }
            }
            vo.setAvgHandleTime(totalMinutes / handledReports.size());
        } else {
            vo.setAvgHandleTime(0.0);
        }
        
        return vo;
    }

    private CommentVO convertCommentToVO(Comment comment) {
        CommentVO vo = new CommentVO();
        vo.setCommentId(comment.getCommentId());
        vo.setGameId(comment.getGameId());
        vo.setUserId(comment.getUserId());
        vo.setContent(comment.getContent());
        vo.setAuditStatus(comment.getAuditStatus());
        vo.setHasSensitiveWord(comment.getHasSensitiveWord());
        vo.setCreateTime(comment.getCreateTime());
        
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getUserId, comment.getUserId());
        User user = userMapper.selectOne(userWrapper);
        if (user != null) {
            vo.setNickname(user.getNickname());
        }
        
        LambdaQueryWrapper<Game> gameWrapper = new LambdaQueryWrapper<>();
        gameWrapper.eq(Game::getGameId, comment.getGameId());
        Game game = gameMapper.selectOne(gameWrapper);
        if (game != null) {
            vo.setGameName(game.getGameName());
        }
        
        return vo;
    }

    private ReportVO convertReportToVO(Report report) {
        ReportVO vo = new ReportVO();
        vo.setReportId(report.getReportId());
        vo.setReportType(report.getReportType());
        vo.setTargetId(report.getTargetId());
        vo.setReasonType(report.getReasonType());
        vo.setReasonDetail(report.getReasonDetail());
        vo.setReporterId(report.getReporterId());
        vo.setStatus(report.getStatus());
        vo.setCreateTime(report.getCreateTime());
        
        LambdaQueryWrapper<User> reporterWrapper = new LambdaQueryWrapper<>();
        reporterWrapper.eq(User::getUserId, report.getReporterId());
        User reporter = userMapper.selectOne(reporterWrapper);
        if (reporter != null) {
            vo.setReporterName(reporter.getNickname());
        }
        
        if ("comment".equals(report.getReportType())) {
            LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
            commentWrapper.eq(Comment::getCommentId, report.getTargetId());
            Comment comment = commentMapper.selectOne(commentWrapper);
            if (comment != null) {
                vo.setTargetName("评论: " + (comment.getContent().length() > 20 
                        ? comment.getContent().substring(0, 20) + "..." 
                        : comment.getContent()));
            }
        } else if ("user".equals(report.getReportType())) {
            LambdaQueryWrapper<User> targetWrapper = new LambdaQueryWrapper<>();
            targetWrapper.eq(User::getUserId, report.getTargetId());
            User target = userMapper.selectOne(targetWrapper);
            if (target != null) {
                vo.setTargetName(target.getNickname());
            }
        } else if ("game".equals(report.getReportType())) {
            LambdaQueryWrapper<Game> gameWrapper = new LambdaQueryWrapper<>();
            gameWrapper.eq(Game::getGameId, report.getTargetId());
            Game game = gameMapper.selectOne(gameWrapper);
            if (game != null) {
                vo.setTargetName(game.getGameName());
            }
        }
        
        return vo;
    }
}
