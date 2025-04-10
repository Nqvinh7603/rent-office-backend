package com.nqvinh.noti.consumer.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nqvinh.noti.consumer.domain.event.NotiEvent;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArraySet;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class NotiWebSocketHandler extends TextWebSocketHandler {

    ObjectMapper objectMapper;
    static CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("New WebSocket connection: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("Received WebSocket message: {}", message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session);
        log.info("WebSocket connection closed: {}", session.getId());
    }

    public void sendNotification(NotiEvent notiEvent) {
        try {
            String payload = objectMapper.writeValueAsString(notiEvent);
            for (WebSocketSession session : sessions) {
                session.sendMessage(new TextMessage(payload));
            }
            log.info("Sent WebSocket notification: {}", payload);
        } catch (Exception e) {
            log.error("Failed to send WebSocket message", e);
        }
    }
}