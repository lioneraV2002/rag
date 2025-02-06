package com.example.ragserver.config;

import java.io.IOException;

import com.example.ragserver.model.FinancialRagService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;


/**
 * Configuration class for enabling and configuring WebSocket communication in the application.
 * This class sets up a WebSocket endpoint to handle real-time text-based communication between
 * the client and server. It integrates with the {@link FinancialRagService} to process incoming
 * messages and generate responses using a Retrieval-Augmented Generation (RAG) model.
 *
 * <p>The WebSocket endpoint is registered at the path {@code /socket-chat}. When a client sends
 * a message to this endpoint, the server processes the message using the {@link FinancialRagService},
 * and sends back the response as a WebSocket message.</p>
 *
 * <p>Key features:</p>
 * <ul>
 *     <li>Enables WebSocket communication using Spring's {@link EnableWebSocket} annotation.</li>
 *     <li>Registers a custom {@link TextWebSocketHandler} to handle incoming text messages.</li>
 *     <li>Uses the {@link FinancialRagService} to process queries and generate responses.</li>
 *     <li>Supports real-time, bidirectional communication over the WebSocket protocol.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * Client connects to: ws://localhost:8080/socket-chat
 * Client sends a message: "What is the revenue for Q1?"
 * Server responds with: "The revenue for Q1 was $10 million."
 * </pre>
 *
 * @see WebSocketConfigurer
 * @see TextWebSocketHandler
 * @see FinancialRagService
 * @see EnableWebSocket
 */

@Configuration
@EnableWebSocket
class WebSocketConfiguration implements WebSocketConfigurer {


	private final FinancialRagService documentService;

    WebSocketConfiguration(FinancialRagService documentService) {
        this.documentService = documentService;
    }

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