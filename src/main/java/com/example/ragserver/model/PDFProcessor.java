package com.example.ragserver.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PDFProcessor {
    private final PGVectorDB pgVectorDB;
    private final String directory;


    public PDFProcessor() {
        this.pgVectorDB = new PGVectorDB();
        this.directory = Constants.PDF_DIRECTORY;
    }

    public void embedPDFs() {

        File folder = new File(directory);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

        if (files != null) {
            for (File file : files) {
                try {
                    String content = extractTextFromPDF(file);
                    pgVectorDB.insertEmbedding(file.getName(), content);
                } catch (Exception e) {
                    System.err.println("Error embedding " + file.getName());
                }
            }
        }
    }

    private String extractTextFromPDF(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            return new PDFTextStripper().getText(document);
        }
    }
}
