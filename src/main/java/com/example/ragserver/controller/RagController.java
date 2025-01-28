package com.example.ragserver.controller;

import com.example.ragserver.model.FinancialRag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling requests related to the RAG service.
 * This controller provides endpoints for uploading PDF documents and querying them.
 */
@RestController
@RequestMapping("rag")
public class RagController {

    private final FinancialRag financialRag;

    /**
     * Constructs a RagController with the specified FinancialRag service.
     *
     * @param financialRag the FinancialRag service for processing PDF uploads and queries.
     */
    @Autowired
    public RagController(FinancialRag financialRag) {
        this.financialRag = financialRag;
    }

    /**
     * Endpoint for uploading a PDF document.
     *
     * @param path the file path of the PDF document to be uploaded.
     * @return a response entity indicating the result of the upload operation.
     */
    @PostMapping("/uploadPDF")
    public ResponseEntity uploadPDF(@RequestBody String path) {
        try {
            financialRag.uploadPDF(path);
            return ResponseEntity.ok().body("Done!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint for querying the uploaded documents.
     *
     * @param question the query string to search for in the documents.
     * @return a response entity containing the generated response based on the query.
     */
    @PostMapping("/query")
    public ResponseEntity query(@RequestBody String question) {
        try {
            String response = financialRag.query(question);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}