package com.example.ragserver.service;

import com.example.ragserver.model.FinancialRagAgent;
import com.example.ragserver.model.TXTProcessor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FinancialRagService {
    private final FinancialRagAgent ragAgent;
    private final TXTProcessor txtProcessor;

    public FinancialRagService() {
        this.ragAgent = new FinancialRagAgent();
        this.txtProcessor = new TXTProcessor();
    }

    public void processTexts() {
        txtProcessor.embedTXTFiles();
    }

    public synchronized void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String input = in.readLine();
            System.out.println("socket with remote address:" + clientSocket.getRemoteSocketAddress() +
                    ", sent query: " + input);
            String response = ragAgent.generateResponse(input);
            out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
