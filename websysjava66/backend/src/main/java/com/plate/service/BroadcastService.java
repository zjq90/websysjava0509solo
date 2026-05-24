package com.plate.service;

import com.plate.entity.BroadcastMessage;
import com.plate.repository.BroadcastMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BroadcastService {
    @Autowired
    private BroadcastMessageRepository messageRepository;

    public BroadcastMessage sendBroadcast(String content, String sentBy) {
        BroadcastMessage message = new BroadcastMessage();
        message.setContent(content);
        message.setSentBy(sentBy);
        return messageRepository.save(message);
    }

    public List<BroadcastMessage> getAllMessages() {
        return messageRepository.findAll();
    }
}
