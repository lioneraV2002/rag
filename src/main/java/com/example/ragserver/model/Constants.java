package com.example.ragserver.model;

public class Constants {
    public static final int SERVER_PORT = 8080;
    public static final String PGVECTOR_DB_URL = "jdbc:postgresql://pgvector:5432/ragdb";
    public static final String OLLAMA_API_URL = "http://ollama:8000";
    public static final String PDF_DIRECTORY = "/data/pdfs";
    public static final String PG_VECTOR_DB_USER = "raguser";
    public static final String PG_DB_PASSWORD = "ragpassword";
    public static final int THREADSPOOL_NUM = 10;
}
