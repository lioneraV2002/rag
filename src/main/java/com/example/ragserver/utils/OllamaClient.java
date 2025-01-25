package com.example.ragserver.utils;

import com.example.ragserver.model.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OllamaClient {
    private final String apiUrl;

    public OllamaClient() {
        this.apiUrl = Constants.OLLAMA_API_URL;
    }

    public String process(String input, String context) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Include all necessary parameters
            // this request only works with llama3.2
            String payload = "{\"model\": \"llama3.2:1b\", " +
                    "\"prompt\": \"" + input + "\"," +
                    " \"context\": \"" + context + "\"," +
                    " \"stream\": false}";
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