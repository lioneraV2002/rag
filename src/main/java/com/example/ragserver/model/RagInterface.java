package com.example.ragserver.model;

import org.springframework.stereotype.Service;

/**
 * Interface for the RAG (Retrieval-Augmented Generation) service.
 * This interface defines methods for uploading PDF documents and querying them.
 */

interface RagInterface {
    /**
     * Uploads a PDF document from the specified path.
     *
     * @param path the file path of the PDF document to be uploaded.
     */
    void uploadPDF(String path);

    /**
     * Queries the uploaded documents with the given prompt.
     *
     * @param prompt the query string to search for in the documents.
     * @return the response generated based on the query.
     */
    String query(String prompt);
}