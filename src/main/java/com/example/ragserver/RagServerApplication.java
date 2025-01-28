package com.example.ragserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the RAG server.
 * This class serves as the entry point for the Spring Boot application.
 */


@SpringBootApplication
public class RagServerApplication {

    /**
     * Main method to run the RAG server application.
     *
     * @param args command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(RagServerApplication.class, args);
    }
}