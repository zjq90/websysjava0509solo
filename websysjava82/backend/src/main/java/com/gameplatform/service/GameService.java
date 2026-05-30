package com.gameplatform.service;

import com.gameplatform.entity.Game;
import com.gameplatform.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public List<Game> findAllEnabled() {
        return gameRepository.findByEnabled(true);
    }

    public List<Game> findRewardVideoEnabled() {
        return gameRepository.findByRewardVideoEnabled(true);
    }

    public Optional<Game> findById(Long id) {
        return gameRepository.findById(id);
    }

    public Optional<Game> findByCode(String code) {
        return gameRepository.findByCode(code);
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public void deleteById(Long id) {
        gameRepository.deleteById(id);
    }

    public Game update(Long id, Game game) {
        game.setId(id);
        return gameRepository.save(game);
    }

    public Game updateRewardVideoConfig(Long id, Boolean enabled, String rewardContent, Integer dailyLimit) {
        Optional<Game> opt = gameRepository.findById(id);
        if (opt.isPresent()) {
            Game game = opt.get();
            if (enabled != null) {
                game.setRewardVideoEnabled(enabled);
            }
            if (rewardContent != null) {
                game.setRewardContent(rewardContent);
            }
            if (dailyLimit != null) {
                game.setDailyWatchLimit(dailyLimit);
            }
            return gameRepository.save(game);
        }
        return null;
    }
}
