package com.example.ragserver.utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;

public class OllamaClient {
    private final String apiUrl;

    public OllamaClient(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String process(String input, String context) {
        try {
            URL url = new URL(apiUrl + "/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String payload = "{\"input\": \"" + input + "\", \"context\": \"" + context + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes());
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error processing request";
        }
    }
}
