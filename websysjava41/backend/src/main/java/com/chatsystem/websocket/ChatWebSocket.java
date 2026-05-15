package com.chatsystem.websocket;

import com.alibaba.fastjson.JSON;
import com.chatsystem.entity.ChatMessage;
import com.chatsystem.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天WebSocket服务端
 */
@Component
@ServerEndpoint("/ws/chat/{userId}")
public class ChatWebSocket {

    private static ChatMessageRepository chatMessageRepository;

    @Autowired
    public void setChatMessageRepository(ChatMessageRepository chatMessageRepository) {
        ChatWebSocket.chatMessageRepository = chatMessageRepository;
    }

    /**
     * 在线用户连接集合
     */
    private static final Map<Long, Session> onlineUsers = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用
     */
    @OnOpen
    public void onOpen(@PathParam("userId") Long userId, Session session) {
        onlineUsers.put(userId, session);
        System.out.println("用户 " + userId + " 连接成功，当前在线人数: " + onlineUsers.size());
    }

    /**
     * 连接关闭调用
     */
    @OnClose
    public void onClose(@PathParam("userId") Long userId) {
        onlineUsers.remove(userId);
        System.out.println("用户 " + userId + " 断开连接，当前在线人数: " + onlineUsers.size());
    }

    /**
     * 收到客户端消息调用
     */
    @OnMessage
    public void onMessage(@PathParam("userId") Long userId, String message) {
        try {
            // 解析消息
            Map<String, Object> messageMap = JSON.parseObject(message, Map.class);
            if (messageMap == null) {
                System.err.println("消息解析失败: " + message);
                return;
            }
            
            // 检查必要字段
            Object toUserIdObj = messageMap.get("toUserId");
            Object typeObj = messageMap.get("type");
            if (toUserIdObj == null || typeObj == null) {
                System.err.println("消息缺少必要字段: toUserId 或 type");
                return;
            }
            
            Long toUserId = Long.valueOf(toUserIdObj.toString());
            Integer type = Integer.valueOf(typeObj.toString());
            String content = messageMap.get("content") != null ? messageMap.get("content").toString() : "";
            String fileName = messageMap.get("fileName") != null ? messageMap.get("fileName").toString() : null;
            String fileUrl = messageMap.get("fileUrl") != null ? messageMap.get("fileUrl").toString() : null;
            Long fileSize = messageMap.get("fileSize") != null ? Long.valueOf(messageMap.get("fileSize").toString()) : null;

            // 构建消息对象
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setFromUserId(userId);
            chatMessage.setToUserId(toUserId);
            chatMessage.setType(type);
            chatMessage.setContent(content);
            chatMessage.setFileName(fileName);
            chatMessage.setFileUrl(fileUrl);
            chatMessage.setFileSize(fileSize);
            chatMessage.setStatus(0);
            chatMessage.setSendTime(LocalDateTime.now());
            
            // 保存消息到数据库
            if (chatMessageRepository != null) {
                chatMessageRepository.save(chatMessage);
            } else {
                System.err.println("chatMessageRepository 未初始化，消息无法持久化");
            }

            // 构建发送消息 - 使用HashMap允许null值，发送时转JSON
            Map<String, Object> sendMessage = new HashMap<>();
            sendMessage.put("id", chatMessage.getId());
            sendMessage.put("fromUserId", userId);
            sendMessage.put("toUserId", toUserId);
            sendMessage.put("type", type);
            sendMessage.put("content", content);
            if (fileName != null) {
                sendMessage.put("fileName", fileName);
            }
            if (fileUrl != null) {
                sendMessage.put("fileUrl", fileUrl);
            }
            if (fileSize != null) {
                sendMessage.put("fileSize", fileSize);
            }
            sendMessage.put("sendTime", chatMessage.getSendTime().toString());

            // 如果接收方在线，推送消息
            Session toSession = onlineUsers.get(toUserId);
            if (toSession != null && toSession.isOpen()) {
                toSession.getBasicRemote().sendText(JSON.toJSONString(sendMessage));
            }

            // 发送给自己同步
            Session fromSession = onlineUsers.get(userId);
            if (fromSession != null && fromSession.isOpen()) {
                fromSession.getBasicRemote().sendText(JSON.toJSONString(sendMessage));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发生错误调用
     */
    @OnError
    public void onError(@PathParam("userId") Long userId, Throwable error) {
        System.out.println("用户 " + userId + " 发生错误: " + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 发送消息给指定用户
     */
    public static void sendMessageToUser(Long userId, String message) throws IOException {
        Session session = onlineUsers.get(userId);
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 检查用户是否在线
     */
    public static boolean isUserOnline(Long userId) {
        return onlineUsers.containsKey(userId);
    }
}
