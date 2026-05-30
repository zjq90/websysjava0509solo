package com.gameplatform.repository;

import com.gameplatform.entity.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long> {
    List<MessageTemplate> findByEnabled(Boolean enabled);
    List<MessageTemplate> findByTemplateType(String templateType);
    List<MessageTemplate> findBySystem(Boolean system);
}
