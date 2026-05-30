package com.gamesys.service;

import com.gamesys.entity.Recommendation;

import java.util.List;

public interface RecommendationService {
    List<Recommendation> getRecommendations(String type, Long categoryId);
    void saveRecommendation(Recommendation recommendation);
    void updateRecommendation(Recommendation recommendation);
    void deleteRecommendation(Long id);
    List<Recommendation> getHomeRecommendations();
    List<Recommendation> getCategoryRecommendations(Long categoryId);
    Recommendation getPopupRecommendation();
    void validateRecommendations();
}
