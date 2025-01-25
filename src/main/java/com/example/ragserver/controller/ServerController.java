package com.example.ragserver.controller;

import com.example.ragserver.model.Constants;
import com.example.ragserver.service.FinancialRagService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {
    private static final int PORT = Constants.SERVER_PORT;
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(Constants.THREADSPOOL_NUM);
    private final FinancialRagService financialRagService;
    private volatile boolean running = true;

    public ServerController() {
        this.financialRagService = new FinancialRagService();
    }

    /**
     * Starts the server and listens for client connections.
     */
    public void startServer() {
        // Process texts before accepting clients
        System.out.println("importing text embeddings into database.");
        financialRagService.processTexts();
        System.out.println("imported text embeddings into database.");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server running on port " + PORT + "...");

            // Listen for incoming connections
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    THREAD_POOL.execute(() -> financialRagService.handleClient(clientSocket));
                } catch (IOException e) {
                    if (!running) {
                        System.out.println("Server shutting down...");
                        break;
                    }
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Error starting server on port " + PORT);
            e.printStackTrace();
        } finally {
            shutdownServer();
        }
    }

    /**
     * Cleans up resources during server shutdown.
     */
    private void shutdownServer() {
        try {
            THREAD_POOL.shutdownNow();
        } catch (Exception e) {
            System.err.println("Error during thread pool shutdown.");
            e.printStackTrace();
        }
    }
}
