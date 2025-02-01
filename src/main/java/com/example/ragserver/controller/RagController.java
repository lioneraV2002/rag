package com.example.ragserver.controller;

import com.example.ragserver.model.FinancialRag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling requests related to the RAG service.
 * This controller provides endpoints for uploading PDF documents and querying them.
 */

@RestController
@RequestMapping("rag")
public class RagController {

    private static final Logger logger = LoggerFactory.getLogger(RagController.class);
    private final FinancialRag financialRag;

    /**
     * Constructs a RagController with the specified FinancialRag service.
     * @param vectorStore the postgres vector store for document storage and retrieval.
     * @param builder     the chat client builder for generating responses.
     */
    public RagController(ChatClient.Builder builder, PgVectorStore vectorStore) {
        logger.info("Initializing RagController");
        this.financialRag = new FinancialRag(builder, vectorStore);
        logger.info("Initialized RagController");

    }


    /**
     * Endpoint for querying the uploaded documents.
     *
     * @param question the query string to search for in the documents.
     * @return a response entity containing the generated response based on the query.
     */
    @PostMapping("/query")
    public ResponseEntity<String> query(@RequestParam(value = "prompt") String question) {
        try {
            logger.info("RagController query question is: {}", question);
            // sending prompt to financial rag service to process it
            String response = financialRag.query(question);
            logger.info("RagController query response is: {}", response);
            logger.info("Returning response <{}> to user", response);
            // sending the response back to user
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // if there were any errors, print them and send and error message to user
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}