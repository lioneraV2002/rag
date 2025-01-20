package com.example.ragserver.controller;

import com.example.ragserver.model.Constants;
import com.example.ragserver.service.RagService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {
    private static final int PORT = Constants.SERVER_PORT;
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);
    private final RagService ragService;

    public ServerController() {
        this.ragService = new RagService();
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server running on port " + PORT + "...");

            // Process PDFs before accepting clients
            ragService.processPDFs();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                THREAD_POOL.execute(() -> ragService.handleClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
