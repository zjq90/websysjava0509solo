package com.music.platform.controller;

import com.music.platform.common.Result;
import com.music.platform.entity.Music;
import com.music.platform.entity.Tag;
import com.music.platform.service.MusicService;
import com.music.platform.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final MusicService musicService;

    @GetMapping
    public Result<List<Tag>> findAll() {
        return Result.success(tagService.findAll());
    }

    @GetMapping("/search")
    public Result<List<Tag>> search(@RequestParam String name) {
        return Result.success(tagService.search(name));
    }

    @GetMapping("/user/{userId}")
    public Result<List<Tag>> findByUser(@PathVariable Long userId) {
        return Result.success(tagService.findByUser(userId));
    }

    @GetMapping("/{id}/music")
    public Result<List<Music>> getMusicByTag(@PathVariable Long id) {
        return Result.success(musicService.getMusicByTag(id));
    }

    @PostMapping
    public Result<Tag> create(@RequestBody Map<String, Object> body) {
        Tag tag = tagService.create(
                (String) body.get("name"),
                body.get("userId") != null ? Long.valueOf(body.get("userId").toString()) : null
        );
        return Result.success(tag);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return Result.success();
    }
}
