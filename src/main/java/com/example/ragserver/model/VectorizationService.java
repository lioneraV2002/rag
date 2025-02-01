package com.example.ragserver.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * VectorizationService is a Spring component that handles the ingestion and vectorization
 * of text data extracted from a PDF document. It implements the CommandLineRunner interface,
 * allowing it to execute specific logic upon application startup.
 *
 * <p>This service reads a PDF file specified in the application properties, splits the
 * extracted text into manageable segments using a text splitter, and stores the resulting
 * vectors in a VectorStore for further processing or retrieval.</p>
 *
 * <p>Key components of this service include:</p>
 * <ul>
 *     <li>{@link ParagraphPdfDocumentReader}: A utility for reading and extracting paragraphs
 *     from the specified PDF document.</li>
 *     <li>{@link TextSplitter}: An interface for splitting text into smaller segments.
 *     In this implementation, a {@link TokenTextSplitter} is used to perform the splitting.</li>
 *     <li>{@link VectorStore}: A storage mechanism for persisting the vector representations
 *     of the text segments.</li>
 * </ul>
 *
 * <p>Upon execution, the service logs the progress of the vectorization process, including
 * the start and completion of text splitting and vector store ingestion.</p>
 *
 * <p>Usage:</p>
 * <pre>
 *     VectorizationService vectorizationService = new VectorizationService(vectorStore);
 *     vectorizationService.run();
 * </pre>
 *
 * <p>Note: Ensure that the PDF file is correctly placed in the specified classpath location
 * for successful execution.</p>
 */



@Component
public class VectorizationService implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(VectorizationService.class);


    private final VectorStore vectorStore;

    // relative address of the file we are using as our base knowledge.
    @Value("classpath:/Docs/article_thebeatoct2024.pdf")
    private Resource pdf;


    public VectorizationService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(String... args) {
        log.info("Vectorization Service started");
        var pdfReader = new ParagraphPdfDocumentReader(pdf);
        TextSplitter textSplitter = new TokenTextSplitter();
        log.info("Text Splitter started");
        vectorStore.accept(textSplitter.apply(pdfReader.get()));
        log.info("Text Splitter finished");
        log.info("Vector Store ingestion complete.");
    }
}
