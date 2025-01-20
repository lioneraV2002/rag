package com.example.ragserver.model;

import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PGVectorDB {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public PGVectorDB() {
        this.dbUrl = Constants.PGVECTOR_DB_URL;
        this.dbUser = Constants.PG_VECTOR_DB_USER;
        this.dbPassword = Constants.PG_DB_PASSWORD;
        this.initializeDatabase();
    }

    /**
     * Inserts an embedding into the database.
     *
     * @param key     Identifier for the embedding (e.g., file name or content description).
     * @param content The text content to be vectorized and stored.
     */
    public void insertEmbedding(String key, String content) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            // Replace this with the actual vectorization logic.
            List<Float> embedding = fakeVectorize(content);

            String sql = "INSERT INTO embeddings (key, vector, content) " +
                    "VALUES (?, ?, ?) " +
                    "ON CONFLICT (key) DO NOTHING;";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, key);
                stmt.setArray(2, connection.createArrayOf("float4", embedding.toArray()));
                stmt.setString(3, content); // Store the original content
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Queries the database for the top-N closest vectors to the input.
     *
     * @param input The input text to query against the stored embeddings.
     * @param topN  Number of closest matches to retrieve.
     * @return A list of relevant contexts with similarity scores.
     */
    public List<ContextResult> query(String input, int topN) {
        List<ContextResult> results = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            // Replace this with the actual vectorization logic.
            List<Float> queryVector = fakeVectorize(input);

            String sql = "SELECT key, content, 1 - (vector <-> ?) AS similarity " +
                    "FROM embeddings " +
                    "ORDER BY vector <-> ? ASC LIMIT ?;";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setArray(1, connection.createArrayOf("float4", queryVector.toArray()));
                stmt.setArray(2, connection.createArrayOf("float4", queryVector.toArray()));
                stmt.setInt(3, topN);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String key = rs.getString("key");
                    String content = rs.getString("content");
                    double similarity = rs.getDouble("similarity");
                    results.add(new ContextResult(key, content, similarity));
                }
            }
        } catch (PSQLException e) {
            System.err.println("Ensure pgvector extension is installed in the database.");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }


    /**
     * Simulates vectorization of text. Replace this with actual vectorization logic.
     *
     * @param text The input text to vectorize.
     * @return A dummy float array representing the vector.
     */
    private List<Float> fakeVectorize(String text) {
        int dim = 128; // Example vector size
        List<Float> vector = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            float value = (i < text.length()) ? (float) text.charAt(i) / 256.0f : 0.0f;
            vector.add(value);
        }
        return vector;
    }


    /**
     * Initializes the database by creating necessary tables.
     */
    private void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS embeddings (" +
                "id SERIAL PRIMARY KEY, " +
                "key TEXT NOT NULL UNIQUE, " +
                "vector VECTOR (128) NOT NULL, " +
                "content TEXT NOT NULL);";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Class representing the result of a query.
     */
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
