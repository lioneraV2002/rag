package com.example.ragserver.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation of the RagInterface for financial document processing.
 * This class handles the uploading of PDF documents and querying them for financial insights.
 */
public class FinancialRag implements RagInterface {

    private static final Logger logger = LoggerFactory.getLogger(FinancialRag.class);
    private final ChatClient client;

    /**
     * Constructs a FinancialRag service with the specified vector store and chat client.
     *
     * @param vectorStore the postgres vector store for document storage and retrieval.
     * @param builder     the chat client builder for generating responses.
     */

    public FinancialRag(ChatClient.Builder builder, PgVectorStore vectorStore) {
        logger.info("Creating FinancialRag");
        this.client = builder
                // use the pdf as part of the context from the pgvector database
                .defaultSystem(RagModelsConstants.FINANCIAL_RAG_MESSAGE_TO_MODEL.getValue())
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();
        logger.info("Created FinancialRag");
    }


    /**
     * Queries the uploaded documents with the given prompt and returns a generated response.
     *
     * @param prompt the query string to search for in the documents.
     * @return the generated response based on the query and context from the documents.
     */
    @Override
    public String query(String prompt) {
        logger.info("Querying FinancialRag with prompt: {}", prompt);
        return client.prompt().user(prompt).call().content();
    }

}