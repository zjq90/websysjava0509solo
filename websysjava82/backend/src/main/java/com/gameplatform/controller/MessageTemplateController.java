package com.gameplatform.controller;

import com.gameplatform.common.Result;
import com.gameplatform.entity.MessageTemplate;
import com.gameplatform.service.MessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message-templates")
public class MessageTemplateController {
    @Autowired
    private MessageTemplateService messageTemplateService;

    @GetMapping
    public Result<List<MessageTemplate>> findAll() {
        return Result.success(messageTemplateService.findAll());
    }

    @GetMapping("/enabled")
    public Result<List<MessageTemplate>> findAllEnabled() {
        return Result.success(messageTemplateService.findAllEnabled());
    }

    @GetMapping("/type/{type}")
    public Result<List<MessageTemplate>> findByType(@PathVariable String type) {
        return Result.success(messageTemplateService.findByType(type));
    }

    @GetMapping("/system")
    public Result<List<MessageTemplate>> findSystemTemplates() {
        return Result.success(messageTemplateService.findSystemTemplates());
    }

    @GetMapping("/{id}")
    public Result<MessageTemplate> findById(@PathVariable Long id) {
        return messageTemplateService.findById(id)
                .map(Result::success)
                .orElse(Result.error("模板不存在"));
    }

    @PostMapping
    public Result<MessageTemplate> save(@RequestBody MessageTemplate template) {
        return Result.success(messageTemplateService.save(template));
    }

    @PutMapping("/{id}")
    public Result<MessageTemplate> update(@PathVariable Long id, @RequestBody MessageTemplate template) {
        return Result.success(messageTemplateService.update(id, template));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        messageTemplateService.deleteById(id);
        return Result.success();
    }
}
