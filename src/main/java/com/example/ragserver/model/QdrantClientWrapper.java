package com.example.ragserver.model;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.ValueFactory;
import io.qdrant.client.VectorsFactory;
import io.qdrant.client.grpc.Collections.Distance;
import io.qdrant.client.grpc.Collections.VectorParams;
import io.qdrant.client.grpc.Points;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class QdrantClientWrapper {
    private final QdrantClient client;

    // The single instance of QdrantClientWrapper
    private static QdrantClientWrapper instance;


    static {
        // create collection
        createCollection();
    }


    // Private constructor to prevent instantiation
    private QdrantClientWrapper() {
        // The Java client uses Qdrant's gRPC interface
        // create a new collections
        this.client = new QdrantClient(
                QdrantGrpcClient.
                        newBuilder(System.getenv("QDRANT_CONTAINER_NAME"),
                                Integer.parseInt(System.getenv("QDRANT_PORT")),
                                false)
                        .build()
        );

    }

    // Public method to get the single instance of QdrantClientWrapper
    public static QdrantClientWrapper getInstance() {
        if (instance == null) {
            instance = new QdrantClientWrapper();
        }
        return instance;
    }

    // Method to create a collection
    private static void createCollection() {
        try {
            getInstance().getClient().createCollectionAsync(Constants.vectorCollectionName,
                    VectorParams.newBuilder().setDistance(Distance.Dot).setSize(Constants.vectorSize).build()
            ).get();
            System.out.println("Collection created: " + Constants.vectorCollectionName);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public void insertEmbedding(String content) {
        List<Float> embedding = fakeVectorize(content);
        System.out.println(content + "\n---> " + embedding);

        List<Points.PointStruct> points = List.of(Points.PointStruct.newBuilder().setVectors(VectorsFactory.vectors(embedding)).putAllPayload(Map.of("content", ValueFactory.value(content))).build());

        try {
            Points.UpdateResult result = client.upsertAsync(Constants.vectorCollectionName, points).get();
            System.out.println("Information inserted: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public List<ContextResult> query(String input, int topN) {
        List<Float> queryVector = fakeVectorize(input);

        Points.SearchPoints searchPoints = Points.SearchPoints.newBuilder().setCollectionName(Constants.vectorCollectionName).addAllVector(queryVector).setLimit(topN).build();

        List<ContextResult> results = new ArrayList<>();

        try {
            List<Points.ScoredPoint> points = client.searchAsync(searchPoints).get();
            for (Points.ScoredPoint point : points) {
                String key = point.getId().getUuid();
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
        int dim = Constants.vectorSize; // Example vector size using enum
        List<Float> vector = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            float value = (i < text.length()) ? (float) text.charAt(i) / 256.0f : 0.0f;
            vector.add(value);
        }
        return vector;
    }

    public QdrantClient getClient() {
        return client;
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