package com.example.ragserver.model;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


/**
 * Service class responsible for handling document uploads and querying using Retrieval-Augmented Generation (RAG).
 * This class implements the {@link RagInterface} and provides functionality to upload PDF documents, tokenize their content,
 * and store the resulting tokens in a vector store. It also supports querying the RAG model to generate responses based on
 * the uploaded documents.
 *
 * <p>Key functionalities:</p>
 * <ul>
 *     <li><b>PDF Upload:</b> Reads and tokenizes PDF documents using {@link PagePdfDocumentReader} and {@link TokenTextSplitter},
 *     then stores the tokens in a {@link VectorStore} for retrieval.</li>
 *     <li><b>Query Handling:</b> Uses a {@link ChatClient} to process user queries and generate responses based on the content
 *     stored in the vector store.</li>
 * </ul>
 *
 * <p>Workflow:</p>
 * <ol>
 *     <li>A PDF document is uploaded via the {@link #uploadPDF(Resource)} method.</li>
 *     <li>The document is read, tokenized, and stored in the vector store.</li>
 *     <li>User queries are processed using the {@link #query(String)} method, which leverages the RAG model to generate responses.</li>
 * </ol>
 *
 * <p>Dependencies:</p>
 * <ul>
 *     <li>{@link ChatClient}: Used to interact with the RAG model for querying.</li>
 *     <li>{@link VectorStore}: Stores tokenized document content for retrieval.</li>
 *     <li>{@link PagePdfDocumentReader}: Reads and extracts content from PDF documents.</li>
 *     <li>{@link TokenTextSplitter}: Splits document content into tokens for processing.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * // Upload a PDF document
 * financialRagService.uploadPDF(pdfResource);
 *
 * // Query the RAG model
 * String response = financialRagService.query("What is the revenue for Q1?");
 * </pre>
 *
 * @see RagInterface
 * @see ChatClient
 * @see VectorStore
 * @see PagePdfDocumentReader
 * @see TokenTextSplitter
 */

@Service
public class FinancialRagService implements RagInterface {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final TokenTextSplitter tokenSplitter = new TokenTextSplitter();

    // Constructor injection
    FinancialRagService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    public void uploadPDF(Resource resource) {
        try {
            // reading pdf file
            var pdfReader = new PagePdfDocumentReader(resource, PdfDocumentReaderConfig.defaultConfig());

            // tokenizing it
            var tokens = tokenSplitter.split(pdfReader.read());

            if (tokens.isEmpty()) {
                throw new IllegalStateException("No tokens were extracted from the PDF.");
            }
            // vectors ingestion to database
            vectorStore.write(tokens);

            System.out.println("Document successfully uploaded to vector store!");
            System.out.println("Tokens count: " + tokens.size());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load document: " + e.getMessage(), e);
        }
    }

    @Override
    public String query(String question) {
        return chatClient
                .prompt()
                .user(question)
                .call()
                .content();
    }
}