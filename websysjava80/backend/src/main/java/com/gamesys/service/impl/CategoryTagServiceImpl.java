package com.gamesys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gamesys.entity.Category;
import com.gamesys.entity.Game;
import com.gamesys.entity.Tag;
import com.gamesys.mapper.CategoryMapper;
import com.gamesys.mapper.GameMapper;
import com.gamesys.mapper.TagMapper;
import com.gamesys.service.CategoryTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryTagServiceImpl implements CategoryTagService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private GameMapper gameMapper;

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.selectList(
                new LambdaQueryWrapper<Category>().orderByAsc(Category::getSort));
    }

    @Override
    public void saveCategory(Category category) {
        category.setGameCount(0);
        categoryMapper.insert(category);
    }

    @Override
    public void updateCategory(Category category) {
        categoryMapper.updateById(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Long count = gameMapper.selectCount(
                new LambdaQueryWrapper<Game>().eq(Game::getCategoryId, id));
        if (count > 0) {
            throw new IllegalArgumentException("该分类下还有游戏，无法删除");
        }
        categoryMapper.deleteById(id);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagMapper.selectList(
                new LambdaQueryWrapper<Tag>().orderByDesc(Tag::getGameCount));
    }

    @Override
    public void saveTag(Tag tag) {
        tag.setGameCount(0);
        tagMapper.insert(tag);
    }

    @Override
    public void updateTag(Tag tag) {
        tagMapper.updateById(tag);
    }

    @Override
    public void deleteTag(Long id) {
        Tag tag = tagMapper.selectById(id);
        if (tag != null && tag.getGameCount() > 0) {
            throw new IllegalArgumentException("该标签已关联游戏，无法删除");
        }
        tagMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mergeTags(Long targetId, List<Long> sourceIds) {
        Tag target = tagMapper.selectById(targetId);
        if (target == null) {
            throw new IllegalArgumentException("目标标签不存在");
        }

        for (Long sourceId : sourceIds) {
            if (sourceId.equals(targetId)) continue;

            Tag source = tagMapper.selectById(sourceId);
            if (source == null) continue;

            String sourceTagName = source.getName();
            String sourceTagAlias = source.getAlias();

            List<Game> games = gameMapper.selectList(
                    new LambdaQueryWrapper<Game>().like(Game::getTags, sourceTagName));

            for (Game game : games) {
                String[] tags = game.getTags().split(",");
                StringBuilder newTags = new StringBuilder();
                for (String t : tags) {
                    if (t.equals(sourceTagName) || (sourceTagAlias != null && t.equals(sourceTagAlias))) {
                        if (!newTags.toString().contains(target.getName())) {
                            if (newTags.length() > 0) newTags.append(",");
                            newTags.append(target.getName());
                        }
                    } else {
                        if (newTags.length() > 0) newTags.append(",");
                        newTags.append(t);
                    }
                }
                game.setTags(newTags.toString());
                gameMapper.updateById(game);
            }

            target.setGameCount(target.getGameCount() + source.getGameCount());
            tagMapper.deleteById(sourceId);
        }

        tagMapper.updateById(target);
    }

    @Override
    public void cleanLowUsageTags(Integer minGameCount) {
        List<Tag> tags = tagMapper.selectList(
                new LambdaQueryWrapper<Tag>().lt(Tag::getGameCount, minGameCount));

        for (Tag tag : tags) {
            tagMapper.deleteById(tag.getId());
        }
    }
}
