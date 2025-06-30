package com.products.productservice.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling chat-related requests.
 * This class uses a ChatClient to process user messages and return AI-generated responses.
 */
@RestController
public class ChatController {

    private final ChatClient chatClient;

    /**
     * Constructor for ChatController.
     *
     * @param chatClient The ChatClient instance used to interact with the AI chat service.
     */
    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient; // Initialize the chat client
    }

    /**
     * Endpoint to handle chat messages.
     *
     * @param message The user-provided message to be sent to the AI chat service.
     * @return The AI-generated response content.
     */
    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt().user(message).call().content();
    }
}