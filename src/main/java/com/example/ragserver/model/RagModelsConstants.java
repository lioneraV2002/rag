package com.example.ragserver.model;

/**
 * Enum for storing some of the constant variables we are going to use in the project.
 * This Enum defines some of them.
 */

public enum RagModelsConstants {
    FINANCIAL_RAG_MESSAGE_TO_MODEL("""
            You are an assistant providing financial and trading support.
            Use the information from the DOCUMENTS section to provide accurate financial suggestions,
            summaries, and analyses for the question in the QUESTION section.
            If there is no relevant information provided in the DOCUMENTS, simply state that you don't know.
          """),
    RAG_SIMILAR_ANSWERS_COUNT(20);


    private final Object value;

    RagModelsConstants(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
