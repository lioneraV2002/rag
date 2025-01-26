package com.example.ragserver.model;


import io.qdrant.client.QdrantClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class QdrantClientWrapper {
    private final QdrantClient client;

    // The single instance of QdrantClientWrapper
    private static QdrantClientWrapper instance;

    // Private constructor to prevent instantiation
    private QdrantClientWrapper() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6333).usePlaintext().build();
        this.client = new QdrantClient(QdrantGrpcClient.newBuilder(channel).build());
    }

    // Public method to get the single instance of QdrantClientWrapper
    public static QdrantClientWrapper getInstance() {
        if (instance == null) {
            instance = new QdrantClientWrapper();
        }
        return instance;
    }

    public void insertEmbedding(String key, String content) {
        List<Float> embedding = fakeVectorize(content);
        System.out.println(content + "\n---> " + embedding);

        List<PointStruct> points = List.of(PointStruct.newBuilder().setId(PointIdFactory.id(key)).setVectors(VectorsFactory.vectors(embedding)).putAllPayload(Map.of("content", ValueFactory.value(content))).build());

        try {
            UpdateResult result = client.upsertAsync("YOUR_COLLECTION_NAME", points).get();
            System.out.println("Information inserted: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public List<ContextResult> query(String input, int topN) {
        List<Float> queryVector = fakeVectorize(input);

        SearchPoints searchPoints = SearchPoints.newBuilder().setCollectionName("YOUR_COLLECTION_NAME").addAllVector(queryVector).setLimit(topN).build();

        List<ContextResult> results = new ArrayList<>();

        try {
            List<ScoredPoint> points = client.searchAsync(searchPoints).get();
            for (ScoredPoint point : points) {
                String key = point.getId().getId();
                String content = point.getPayload().get("content").getStringValue();
                double similarity = point.getScore();
                results.add(new ContextResult(key, content, similarity));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return results;
    }

    private List<Float> fakeVectorize(String text) {
        int dim = 128; // Example vector size
        List<Float> vector = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            float value = (i < text.length()) ? (float) text.charAt(i) / 256.0f : 0.0f;
            vector.add(value);
        }
        return vector;
    }

    public static class ContextResult {
        private final String key;
        private final String content;
        private final double similarity;

        public ContextResult(String key, String content, double similarity) {
            this.key = key;
            this.content = content;
            this.similarity = similarity;
        }

        @Override
        public String toString() {
            return String.format("Key: %s, Similarity: %.3f, Content: %s", key, similarity, content);
        }
    }
}
