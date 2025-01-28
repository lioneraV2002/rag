package com.example.ragserver.model;

public enum RagModelsConstants {
    FINANCIAL_RAG_MESSAGE_TO_MODEL("""
            You are an assistant providing financial and trading support.
            Use the information from the DOCUMENTS section to provide accurate financial suggestions,
            summaries, and analyses for the question in the QUESTION section.
            If unsure, simply state that you don't know.
          """);


    private final String value;

    RagModelsConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
