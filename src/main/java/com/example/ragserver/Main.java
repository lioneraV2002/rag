package com.example.ragserver;

import com.example.ragserver.controller.ServerController;

public class Main {
    public static void main(String[] args) {
        ServerController serverController = new ServerController();
        serverController.startServer();
    }
}
