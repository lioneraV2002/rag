package com.example.ragserver.config;

import java.io.IOException;

import com.example.ragserver.model.FinancialRagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Configuration
@EnableWebSocket
class WebSocketConfiguration implements WebSocketConfigurer {

	@Autowired
	private FinancialRagService documentService;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new TextWebSocketHandler() {
			@Override
			public void handleTextMessage(WebSocketSession session,
					TextMessage message) throws IOException {
				String response = documentService.query(message.getPayload());
				session.sendMessage(new TextMessage(response));
			}
		}, "/socket-chat");
	}
}