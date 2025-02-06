package com.example.ragserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Main entry point for the RAG (Retrieval-Augmented Generation) server application.
 * This class initializes and runs the Spring Boot application, enabling autoconfiguration,
 * component scanning, and other Spring Boot features.
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *     <li>Bootstraps the Spring Boot application using the {@link SpringBootApplication} annotation.</li>
 *     <li>Starts the embedded server and initializes the Spring application context.</li>
 *     <li>Enables autoconfiguration of Spring components, such as WebSocket, controllers, services, and configurations.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <pre>
 * // Run the application
 * Application.main(new String[] {});
 * </pre>
 *
 * <p>This class is typically executed to start the application server, which provides endpoints for
 * document upload, chat functionality, and RAG-based querying.</p>
 *
 * @see SpringBootApplication
 */

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
