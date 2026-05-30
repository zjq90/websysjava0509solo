package com.gameplatform.service;

import com.gameplatform.entity.MessageTemplate;
import com.gameplatform.repository.MessageTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageTemplateService {
    @Autowired
    private MessageTemplateRepository messageTemplateRepository;

    public List<MessageTemplate> findAll() {
        return messageTemplateRepository.findAll();
    }

    public List<MessageTemplate> findAllEnabled() {
        return messageTemplateRepository.findByEnabled(true);
    }

    public List<MessageTemplate> findByType(String type) {
        return messageTemplateRepository.findByTemplateType(type);
    }

    public List<MessageTemplate> findSystemTemplates() {
        return messageTemplateRepository.findBySystem(true);
    }

    public Optional<MessageTemplate> findById(Long id) {
        return messageTemplateRepository.findById(id);
    }

    public MessageTemplate save(MessageTemplate template) {
        return messageTemplateRepository.save(template);
    }

    public void deleteById(Long id) {
        messageTemplateRepository.deleteById(id);
    }

    public MessageTemplate update(Long id, MessageTemplate template) {
        template.setId(id);
        return messageTemplateRepository.save(template);
    }
}
