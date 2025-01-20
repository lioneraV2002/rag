package com.example.ragserver.model;

import java.sql.*;

public class PGVectorDB {
    private final String dbUrl;

    public PGVectorDB(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public void insertEmbedding(String key, String content) {
        try (Connection connection = DriverManager.getConnection(dbUrl, "raguser", "ragpassword");
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO embeddings (key, content) VALUES (?, ?)")) {
            statement.setString(1, key);
            statement.setString(2, content);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String query(String input) {
        // Simulated query (replace with pgvector similarity search)
        return "Simulated vector-based context for: " + input;
    }
}
