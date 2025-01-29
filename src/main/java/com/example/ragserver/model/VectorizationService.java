package com.example.ragserver.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class VectorizationService implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(VectorizationService.class);


    private final VectorStore vectorStore;


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
