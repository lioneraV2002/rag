package com.example.ragserver.model;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class TXTProcessor {
    private final QdrantClientWrapper qdrantdb;
    private final String directory;
    private static final int CHUNK_SIZE = 128; // Fixed size for each chunk (you can adjust this)

    public TXTProcessor() {
        this.qdrantdb  = QdrantClientWrapper.getInstance();
        this.directory = Constants.TXT_DIRECTORY; // Assuming there's a directory constant for TXT files
    }

    public void embedTXTFiles() {
        File folder = new File(directory);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        if (files != null) {
            for (File file : files) {
                try {
                    String content = extractTextFromTXT(file);
                    insertTextInChunks(file.getName(), content);
                    System.out.println("text is imported in table.");
                } catch (Exception e) {
                    System.err.println("Error embedding " + file.getName());
                }
            }
        }
    }

    private String extractTextFromTXT(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private void insertTextInChunks(String fileName, String content) {
        int length = content.length();
        for (int i = 0; i < length; i += CHUNK_SIZE) {
            int end = Math.min(i + CHUNK_SIZE, length);
            String chunk = content.substring(i, end);
            qdrantdb.insertEmbedding(fileName, chunk);
        }
    }
}
