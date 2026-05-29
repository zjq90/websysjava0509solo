package com.music.platform.service;

import com.music.platform.entity.Category;
import com.music.platform.entity.Music;
import com.music.platform.repository.CategoryRepository;
import com.music.platform.repository.MusicCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MusicCategoryRepository musicCategoryRepository;
    private final MusicService musicService;

    public Category create(String name, String type, String icon) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setIcon(icon);
        return categoryRepository.save(category);
    }

    public List<Category> findByType(String type) {
        return categoryRepository.findByType(type);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Music> getMusicByCategory(Long categoryId) {
        return musicService.getMusicByCategory(categoryId);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
