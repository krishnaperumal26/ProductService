package com.products.productservice.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * Configuration class for setting up the OpenAI Chat Client.
 * This class defines a bean for creating and configuring a ChatClient instance
 * using the OpenAiChatModel.
 */
@Configurable
public class OpenAIChatClientConfig {

    /**
     * Creates and configures a ChatClient bean.
     *
     * @param openAiChatModel The OpenAI chat model to be used for the ChatClient.
     * @return A configured instance of ChatClient.
     */
    @Bean
    public ChatClient chatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel)
                .build();
    }
}