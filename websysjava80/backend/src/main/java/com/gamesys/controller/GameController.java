package com.gamesys.controller;

import com.gamesys.common.PageResult;
import com.gamesys.common.Result;
import com.gamesys.dto.GameQueryDTO;
import com.gamesys.entity.Game;
import com.gamesys.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(tags = "游戏管理")
@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @ApiOperation("获取游戏列表")
    @GetMapping
    public Result<PageResult<Game>> getGameList(GameQueryDTO query) {
        return Result.success(gameService.getGameList(query));
    }

    @ApiOperation("获取游戏详情")
    @GetMapping("/{id}")
    public Result<Game> getGameDetail(@PathVariable Long id) {
        return Result.success(gameService.getGameDetail(id));
    }

    @ApiOperation("新增游戏")
    @PostMapping
    public Result<Void> saveGame(@RequestBody Game game) {
        gameService.saveGame(game);
        return Result.success();
    }

    @ApiOperation("更新游戏")
    @PutMapping("/{id}")
    public Result<Void> updateGame(@PathVariable Long id, @RequestBody Game game) {
        game.setId(id);
        gameService.updateGame(game);
        return Result.success();
    }

    @ApiOperation("删除游戏")
    @DeleteMapping("/{id}")
    public Result<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return Result.success();
    }

    @ApiOperation("批量更新状态")
    @PutMapping("/batch/status")
    public Result<Void> batchUpdateStatus(@RequestParam List<Long> ids, @RequestParam Integer status) {
        gameService.batchUpdateStatus(ids, status);
        return Result.success();
    }

    @ApiOperation("批量更新推荐")
    @PutMapping("/batch/recommend")
    public Result<Void> batchUpdateRecommend(@RequestParam List<Long> ids, @RequestParam Integer recommend) {
        gameService.batchUpdateRecommend(ids, recommend);
        return Result.success();
    }

    @ApiOperation("导出游戏列表")
    @GetMapping("/export")
    public void exportGames(GameQueryDTO query, HttpServletResponse response) throws IOException {
        gameService.exportGames(query, response);
    }

    @ApiOperation("获取待审核游戏列表")
    @GetMapping("/pending")
    public Result<PageResult<Game>> getPendingGames(GameQueryDTO query) {
        return Result.success(gameService.getPendingGames(query));
    }
}
