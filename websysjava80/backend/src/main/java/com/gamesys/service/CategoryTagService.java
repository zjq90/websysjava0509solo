package com.gamesys.service;

import com.gamesys.entity.Category;
import com.gamesys.entity.Tag;

import java.util.List;

public interface CategoryTagService {
    List<Category> getAllCategories();
    void saveCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(Long id);

    List<Tag> getAllTags();
    void saveTag(Tag tag);
    void updateTag(Tag tag);
    void deleteTag(Long id);
    void mergeTags(Long targetId, List<Long> sourceIds);
    void cleanLowUsageTags(Integer minGameCount);
}
