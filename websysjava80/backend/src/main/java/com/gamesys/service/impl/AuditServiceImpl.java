package com.gamesys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gamesys.common.GameStatus;
import com.gamesys.common.PageResult;
import com.gamesys.dto.AuditDTO;
import com.gamesys.entity.AuditLog;
import com.gamesys.entity.AutoAuditRule;
import com.gamesys.entity.Game;
import com.gamesys.mapper.AuditLogMapper;
import com.gamesys.mapper.AutoAuditRuleMapper;
import com.gamesys.mapper.GameMapper;
import com.gamesys.service.AuditService;
import com.gamesys.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private AutoAuditRuleMapper autoAuditRuleMapper;

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private GameService gameService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(AuditDTO dto) {
        for (Long gameId : dto.getGameIds()) {
            Game game = gameMapper.selectById(gameId);
            if (game == null) {
                continue;
            }

            game.setStatus(dto.getAuditResult() == 1 ? GameStatus.ONLINE.getCode() : GameStatus.REJECTED.getCode());
            if (dto.getAuditResult() == 1) {
                game.setOnlineTime(LocalDateTime.now());
            }
            gameMapper.updateById(game);

            AuditLog log = new AuditLog();
            log.setGameId(gameId);
            log.setGameName(game.getName());
            log.setAuditorId(dto.getAuditorId());
            log.setAuditorName(dto.getAuditorName() != null ? dto.getAuditorName() : "系统管理员");
            log.setAuditTime(LocalDateTime.now());
            log.setAuditResult(dto.getAuditResult());
            log.setRejectReason(dto.getRejectReason());
            log.setRemark(dto.getRemark());
            auditLogMapper.insert(log);

            redisTemplate.delete("game:" + gameId);
        }
    }

    @Override
    public void autoAudit(Long gameId) {
        Game game = gameMapper.selectById(gameId);
        if (game == null || !GameStatus.PENDING.getCode().equals(game.getStatus())) {
            return;
        }

        List<AutoAuditRule> rules = autoAuditRuleMapper.selectList(
                new LambdaQueryWrapper<AutoAuditRule>().eq(AutoAuditRule::getStatus, 1));

        for (AutoAuditRule rule : rules) {
            if (matchRule(game, rule)) {
                AuditDTO dto = new AuditDTO();
                dto.setGameIds(java.util.Collections.singletonList(gameId));
                dto.setAuditResult(rule.getAction());
                dto.setRemark("自动审核：" + rule.getRuleName());
                dto.setAuditorId(0L);
                dto.setAuditorName("系统自动审核");
                if (rule.getAction() == 0) {
                    dto.setRejectReason("触发自动拒绝规则：" + rule.getRuleName());
                }
                audit(dto);
                return;
            }
        }
    }

    @Override
    public PageResult<AuditLog> getAuditLogs(Long gameId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<AuditLog> wrapper = new LambdaQueryWrapper<>();
        if (gameId != null) {
            wrapper.eq(AuditLog::getGameId, gameId);
        }
        wrapper.orderByDesc(AuditLog::getAuditTime);
        Page<AuditLog> page = new Page<>(pageNum, pageSize);
        auditLogMapper.selectPage(page, wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public List<AutoAuditRule> getAutoAuditRules() {
        return autoAuditRuleMapper.selectList(null);
    }

    @Override
    public void saveAutoAuditRule(AutoAuditRule rule) {
        if (rule.getId() == null) {
            autoAuditRuleMapper.insert(rule);
        } else {
            autoAuditRuleMapper.updateById(rule);
        }
    }

    @Override
    public void deleteAutoAuditRule(Long id) {
        autoAuditRuleMapper.deleteById(id);
    }

    @Override
    public void updateAutoAuditRuleStatus(Long id, Integer status) {
        AutoAuditRule rule = new AutoAuditRule();
        rule.setId(id);
        rule.setStatus(status);
        autoAuditRuleMapper.updateById(rule);
    }

    private boolean matchRule(Game game, AutoAuditRule rule) {
        if ("developer_game_count".equals(rule.getRuleType())) {
            Integer count = gameMapper.selectCount(
                    new LambdaQueryWrapper<Game>()
                            .eq(Game::getDeveloper, game.getDeveloper())
                            .eq(Game::getStatus, GameStatus.ONLINE.getCode())).intValue();
            return count >= Integer.parseInt(rule.getRuleValue());
        } else if ("sensitive_words".equals(rule.getRuleType())) {
            String content = game.getName() + game.getDescription();
            String[] words = rule.getRuleValue().split(",");
            for (String word : words) {
                if (content.contains(word.trim())) {
                    return true;
                }
            }
        }
        return false;
    }
}
