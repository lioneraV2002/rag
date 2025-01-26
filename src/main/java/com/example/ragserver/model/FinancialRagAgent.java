package com.example.ragserver.model;
import com.example.ragserver.utils.OllamaClient;

public class FinancialRagAgent implements RagAgent {
    private final OllamaClient ollamaClient;
    private final QdrantClientWrapper qdrantClientWrapper;

    public FinancialRagAgent() {
        this.ollamaClient = new OllamaClient();
        this.qdrantClientWrapper = QdrantClientWrapper.getInstance();
    }

    @Override
    public String generateResponse(String input) {
        String vectorContext = String.valueOf(qdrantClientWrapper.query(input, 20));
        String ollamaResponse = ollamaClient.process(input, vectorContext);
        return "Agent Response: <" + ollamaResponse + ">\n" + "Context: <" + vectorContext + ">";
    }


}
