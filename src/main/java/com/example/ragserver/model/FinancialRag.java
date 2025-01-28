package com.example.ragserver.model;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.elasticsearch.ElasticsearchVectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation of the RagInterface for financial document processing.
 * This class handles the uploading of PDF documents and querying them for financial insights.
 */
@Service
public class FinancialRag {

    private final ElasticsearchVectorStore vectorStore;
    private final ChatClient client;

    /**
     * Constructs a FinancialRag service with the specified vector store and chat client.
     *
     * @param vectorStore the Elasticsearch vector store for document storage and retrieval.
     * @param client      the chat client builder for generating responses.
     */

    public FinancialRag(ElasticsearchVectorStore vectorStore, ChatClient.Builder client) {
        this.vectorStore = vectorStore;
        this.client = client.build();
    }

    /**
     * Uploads a PDF document from the specified path and processes it for storage.
     *
     * @param path the file path of the PDF document to be uploaded.
     */
//    @Override
    public void uploadPDF(String path) {
        // reading pdf files page by page
        PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(path);
        List<Document> documentPages = pdfDocumentReader.read();

        // tokenizing and sending document pages to vector store
        documentPages = new TokenTextSplitter().apply(documentPages);
        vectorStore.doAdd(documentPages);
    }

    /**
     * Queries the uploaded documents with the given prompt and returns a generated response.
     *
     * @param prompt the query string to search for in the documents.
     * @return the generated response based on the query and context from the documents.
     */
//    @Override
    public String query(String prompt) {
        // getting results based on similarity search
        List<Document> dbResults = vectorStore.doSimilaritySearch(
                SearchRequest
                        .builder()
                        .query(prompt)
                        .topK(Integer.parseInt(RagModelsConstants.RAG_SIMILAR_ANSWERS_COUNT.getValue()))
                        .build());

        // combining results to create a single unified string
        String resultsCombined = dbResults.stream()
                .map(Document::getFormattedContent)
                .collect(Collectors.joining(System.lineSeparator()));

        // generating prompt to send to the model
        String promptWithContext = RagModelsConstants.FINANCIAL_RAG_MESSAGE_TO_MODEL.getValue() +
                "\nContext:\n" + resultsCombined + "\nPROMPT:\n" + prompt + "\n";

        // get model response
        String modelResponse = client.prompt().user(promptWithContext).call().content();

        // send structured response from rag service
        return modelResponse +
                System.lineSeparator() +
                "The response was first found at file:\n" +
                dbResults.getFirst().getMetadata().get(PagePdfDocumentReader.METADATA_FILE_NAME) +
                "And page:\n" +
                dbResults.getFirst().getMetadata().get(PagePdfDocumentReader.METADATA_START_PAGE_NUMBER);
    }
}