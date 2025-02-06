package com.example.ragserver.model;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

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