package com.gamesys.service;

import com.gamesys.common.PageResult;
import com.gamesys.dto.GameExcelVO;
import com.gamesys.dto.GameQueryDTO;
import com.gamesys.entity.Game;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface GameService {
    PageResult<Game> getGameList(GameQueryDTO query);
    Game getGameDetail(Long id);
    void saveGame(Game game);
    void updateGame(Game game);
    void deleteGame(Long id);
    void batchUpdateStatus(List<Long> ids, Integer status);
    void batchUpdateRecommend(List<Long> ids, Integer recommend);
    void exportGames(GameQueryDTO query, HttpServletResponse response) throws IOException;
    PageResult<Game> getPendingGames(GameQueryDTO query);
    List<GameExcelVO> getGameExcelList(GameQueryDTO query);
}
