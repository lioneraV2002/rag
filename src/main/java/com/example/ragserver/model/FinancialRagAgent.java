package com.example.ragserver.model;
import com.example.ragserver.utils.OllamaClient;

public class FinancialRagAgent implements RagAgent {
    private final OllamaClient ollamaClient;
    private final PGVectorDB pgVectorDB;

    public FinancialRagAgent() {
        this.ollamaClient = new OllamaClient(Constants.OLLAMA_API_URL);
        this.pgVectorDB = new PGVectorDB(Constants.PGVECTOR_DB_URL);
    }

    public String generateResponse(String input) {
        String vectorContext = pgVectorDB.query(input);
        String ollamaResponse = ollamaClient.process(input, vectorContext);
        return "Ollama: " + ollamaResponse + " | Context: " + vectorContext;
    }
}
