package com.appsys.service;

import com.appsys.entity.ChatMessage;
import com.appsys.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private static final Map<String, List<String>> AI_RESPONSES = new HashMap<>();

    static {
        AI_RESPONSES.put("无法上网", Arrays.asList(
                "请检查您的光猫是否正常亮灯，PON灯应为常亮状态。",
                "尝试重启光猫和路由器，断电等待30秒后重新通电。",
                "检查网线是否插好，建议重新拔插一下网线。"
        ));
        AI_RESPONSES.put("网速慢", Arrays.asList(
                "请检查是否有多个设备同时在观看视频或下载大文件。",
                "您可以尝试靠近路由器使用，减少障碍物干扰。",
                "建议定期重启路由器，每周至少重启一次。"
        ));
        AI_RESPONSES.put("掉线", Arrays.asList(
                "请检查路由器摆放位置，避免放置在墙角或家电旁边。",
                "尝试更改WiFi信道，减少与邻居WiFi的干扰。",
                "如果问题持续，可能是线路问题，建议联系人工客服。"
        ));
    }

    public List<ChatMessage> getHistoryByUserId(Long userId) {
        return chatMessageRepository.findByUserIdOrderByCreateTimeAsc(userId);
    }

    public List<ChatMessage> getHistoryBySessionId(String sessionId) {
        return chatMessageRepository.findBySessionIdOrderByCreateTimeAsc(sessionId);
    }

    public ChatMessage sendMessage(Long userId, String sessionId, String content) {
        ChatMessage userMessage = new ChatMessage();
        userMessage.setUserId(userId);
        userMessage.setSessionId(sessionId);
        userMessage.setSenderType("USER");
        userMessage.setContent(content);
        userMessage.setIsAi(false);
        chatMessageRepository.save(userMessage);

        ChatMessage aiResponse = new ChatMessage();
        aiResponse.setUserId(userId);
        aiResponse.setSessionId(sessionId);
        aiResponse.setSenderType("AI");
        aiResponse.setContent(generateAIResponse(content));
        aiResponse.setIsAi(true);

        if (shouldTransferToHuman(content)) {
            aiResponse.setTransferToHuman(true);
            aiResponse.setContent(aiResponse.getContent() + "\n\n已为您转接人工客服，请稍候...");
        }

        return chatMessageRepository.save(aiResponse);
    }

    private String generateAIResponse(String userMessage) {
        for (Map.Entry<String, List<String>> entry : AI_RESPONSES.entrySet()) {
            if (userMessage.contains(entry.getKey())) {
                List<String> responses = entry.getValue();
                return responses.get(new Random().nextInt(responses.size()));
            }
        }
        return "感谢您的咨询！我理解您遇到了问题，请您详细描述一下具体情况，我会尽力为您解答。如果需要人工服务，请说\"转人工\"。";
    }

    private boolean shouldTransferToHuman(String userMessage) {
        return userMessage.contains("转人工") || userMessage.contains("人工服务") || userMessage.contains("听不懂");
    }

    public void deleteBySessionId(String sessionId) {
        List<ChatMessage> messages = chatMessageRepository.findBySessionIdOrderByCreateTimeAsc(sessionId);
        chatMessageRepository.deleteAll(messages);
    }
}
