package com.example.ragserver.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


/**
 * Configuration class for setting up different {@link ChatClient} instances based on the active profile.
 * This class provides bean definitions for creating chat clients that integrate with different chat models
 * such as OpenAI and Ollama. Each chat client is configured with default advisors, including a logger advisor
 * and a Retrieval-Augmented Generation (RAG) advisor for question-answering functionality.
 *
 * <p>The class uses Spring's {@link Profile} annotation to conditionally create beans based on the active profile.
 * This allows for flexible configuration depending on the environment or use case.</p>
 *
 * <p>Key configurations:</p>
 * <ul>
 *     <li>{@code openai} profile: Configures a {@link ChatClient} using the {@link OpenAiChatModel} for OpenAI integration.</li>
 *     <li>{@code ollama} profile: Configures a {@link ChatClient} using the {@link OllamaChatModel} for Ollama integration.</li>
 * </ul>
 *
 * <p>Both chat clients are configured with a {@link SimpleLoggerAdvisor} for logging and a {@link QuestionAnswerAdvisor}
 * for RAG-based question-answering, leveraging a {@link VectorStore} for retrieval.</p>
 *
 * @see ChatClient
 * @see OpenAiChatModel
 * @see OllamaChatModel
 * @see VectorStore
 * @see SimpleLoggerAdvisor
 * @see QuestionAnswerAdvisor
 */

@Configuration
public class ChatClientConfigurations {

    @Bean
    @Profile("openai")
    public ChatClient openAIChatClient(OpenAiChatModel chatModel, VectorStore vectorStore) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()) // RAG advisor
                )
                .build();
    }

    @Bean
    @Profile("ollama")
    public ChatClient ollamaChatClient(OllamaChatModel chatModel, VectorStore vectorStore) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()) // RAG advisor
                )
                .build();
    }
}