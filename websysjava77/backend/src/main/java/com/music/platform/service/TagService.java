package com.music.platform.service;

import com.music.platform.entity.Tag;
import com.music.platform.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Tag create(String name, Long userId) {
        Optional<Tag> existing = tagRepository.findByName(name);
        if (existing.isPresent()) {
            return existing.get();
        }
        Tag tag = new Tag();
        tag.setName(name);
        tag.setUserId(userId);
        return tagRepository.save(tag);
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public List<Tag> search(String name) {
        return tagRepository.findByNameContaining(name);
    }

    public List<Tag> findByUser(Long userId) {
        return tagRepository.findByUserId(userId);
    }

    public void delete(Long id) {
        tagRepository.deleteById(id);
    }
}
