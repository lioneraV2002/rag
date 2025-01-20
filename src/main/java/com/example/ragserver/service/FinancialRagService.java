package com.example.ragserver.service;

import com.example.ragserver.model.FinancialRagAgent;
import com.example.ragserver.model.PDFProcessor;

import java.io.*;
import java.net.Socket;

public class FinancialRagService {
    private final FinancialRagAgent ragAgent;
    private final PDFProcessor pdfProcessor;

    public FinancialRagService() {
        this.ragAgent = new FinancialRagAgent();
        this.pdfProcessor = new PDFProcessor();
    }

    public void processPDFs() {
        pdfProcessor.embedPDFs();
    }

    public void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String input = in.readLine();
            String response = ragAgent.generateResponse(input);
            out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
