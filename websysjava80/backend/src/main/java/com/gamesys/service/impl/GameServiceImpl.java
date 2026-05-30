package com.gamesys.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gamesys.common.GameStatus;
import com.gamesys.common.PageResult;
import com.gamesys.dto.GameExcelVO;
import com.gamesys.dto.GameQueryDTO;
import com.gamesys.entity.Game;
import com.gamesys.mapper.GameMapper;
import com.gamesys.service.GameService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String GAME_CACHE_PREFIX = "game:";

    @Override
    public PageResult<Game> getGameList(GameQueryDTO query) {
        LambdaQueryWrapper<Game> wrapper = buildQueryWrapper(query);
        Page<Game> page = new Page<>(query.getPageNum(), query.getPageSize());
        gameMapper.selectPage(page, wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Game getGameDetail(Long id) {
        String cacheKey = GAME_CACHE_PREFIX + id;
        Game cached = (Game) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return cached;
        }
        Game game = gameMapper.selectById(id);
        if (game != null) {
            redisTemplate.opsForValue().set(cacheKey, game, 10, java.util.concurrent.TimeUnit.MINUTES);
        }
        return game;
    }

    @Override
    public void saveGame(Game game) {
        game.setStatus(GameStatus.PENDING.getCode());
        game.setSubmitTime(LocalDateTime.now());
        game.setHotValue(0);
        game.setPlayCount(0);
        game.setRecommend(0);
        gameMapper.insert(game);
    }

    @Override
    public void updateGame(Game game) {
        gameMapper.updateById(game);
        redisTemplate.delete(GAME_CACHE_PREFIX + game.getId());
    }

    @Override
    public void deleteGame(Long id) {
        gameMapper.deleteById(id);
        redisTemplate.delete(GAME_CACHE_PREFIX + id);
    }

    @Override
    public void batchUpdateStatus(List<Long> ids, Integer status) {
        for (Long id : ids) {
            Game game = new Game();
            game.setId(id);
            game.setStatus(status);
            if (GameStatus.ONLINE.getCode().equals(status)) {
                game.setOnlineTime(LocalDateTime.now());
            } else if (GameStatus.OFFLINE.getCode().equals(status)) {
                game.setOfflineTime(LocalDateTime.now());
            }
            gameMapper.updateById(game);
            redisTemplate.delete(GAME_CACHE_PREFIX + id);
        }
    }

    @Override
    public void batchUpdateRecommend(List<Long> ids, Integer recommend) {
        for (Long id : ids) {
            Game game = new Game();
            game.setId(id);
            game.setRecommend(recommend);
            gameMapper.updateById(game);
            redisTemplate.delete(GAME_CACHE_PREFIX + id);
        }
    }

    @Override
    public void exportGames(GameQueryDTO query, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("游戏列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<GameExcelVO> data = getGameExcelList(query);
        EasyExcel.write(response.getOutputStream(), GameExcelVO.class).sheet("游戏列表").doWrite(data);
    }

    @Override
    public PageResult<Game> getPendingGames(GameQueryDTO query) {
        query.setStatus(GameStatus.PENDING.getCode());
        LambdaQueryWrapper<Game> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(Game::getSubmitTime);
        Page<Game> page = new Page<>(query.getPageNum(), query.getPageSize());
        gameMapper.selectPage(page, wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public List<GameExcelVO> getGameExcelList(GameQueryDTO query) {
        LambdaQueryWrapper<Game> wrapper = buildQueryWrapper(query);
        List<Game> games = gameMapper.selectList(wrapper);
        List<GameExcelVO> result = new ArrayList<>();
        for (Game game : games) {
            GameExcelVO vo = new GameExcelVO();
            BeanUtils.copyProperties(game, vo);
            vo.setStatus(getStatusDesc(game.getStatus()));
            result.add(vo);
        }
        return result;
    }

    private LambdaQueryWrapper<Game> buildQueryWrapper(GameQueryDTO query) {
        LambdaQueryWrapper<Game> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(Game::getName, query.getKeyword())
                    .or().like(Game::getDeveloper, query.getKeyword()));
        }
        if (query.getStatus() != null) {
            wrapper.eq(Game::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(Game::getType, query.getType());
        }
        if (query.getCategoryId() != null) {
            wrapper.eq(Game::getCategoryId, query.getCategoryId());
        }
        if (StringUtils.hasText(query.getDeveloper())) {
            wrapper.like(Game::getDeveloper, query.getDeveloper());
        }
        if (query.getStartTime() != null) {
            wrapper.ge(Game::getCreateTime, query.getStartTime());
        }
        if (query.getEndTime() != null) {
            wrapper.le(Game::getCreateTime, query.getEndTime());
        }
        wrapper.orderByDesc(Game::getCreateTime);
        return wrapper;
    }

    private String getStatusDesc(Integer status) {
        for (GameStatus s : GameStatus.values()) {
            if (s.getCode().equals(status)) {
                return s.getDesc();
            }
        }
        return "未知";
    }
}
