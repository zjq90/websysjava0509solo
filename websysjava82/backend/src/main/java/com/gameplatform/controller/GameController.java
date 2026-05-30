package com.gameplatform.controller;

import com.gameplatform.common.Result;
import com.gameplatform.entity.Game;
import com.gameplatform.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping
    public Result<List<Game>> findAll() {
        return Result.success(gameService.findAll());
    }

    @GetMapping("/enabled")
    public Result<List<Game>> findAllEnabled() {
        return Result.success(gameService.findAllEnabled());
    }

    @GetMapping("/reward-video")
    public Result<List<Game>> findRewardVideoEnabled() {
        return Result.success(gameService.findRewardVideoEnabled());
    }

    @GetMapping("/{id}")
    public Result<Game> findById(@PathVariable Long id) {
        return gameService.findById(id)
                .map(Result::success)
                .orElse(Result.error("游戏不存在"));
    }

    @GetMapping("/code/{code}")
    public Result<Game> findByCode(@PathVariable String code) {
        return gameService.findByCode(code)
                .map(Result::success)
                .orElse(Result.error("游戏不存在"));
    }

    @PostMapping
    public Result<Game> save(@RequestBody Game game) {
        return Result.success(gameService.save(game));
    }

    @PutMapping("/{id}")
    public Result<Game> update(@PathVariable Long id, @RequestBody Game game) {
        return Result.success(gameService.update(id, game));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        gameService.deleteById(id);
        return Result.success();
    }

    @PatchMapping("/{id}/reward-video")
    public Result<Game> updateRewardVideoConfig(
            @PathVariable Long id,
            @RequestBody Map<String, Object> config) {
        Boolean enabled = (Boolean) config.get("enabled");
        String rewardContent = (String) config.get("rewardContent");
        Integer dailyLimit = config.get("dailyLimit") != null ? ((Number) config.get("dailyLimit")).intValue() : null;

        Game game = gameService.updateRewardVideoConfig(id, enabled, rewardContent, dailyLimit);
        if (game != null) {
            return Result.success(game);
        }
        return Result.error("游戏不存在");
    }
}
