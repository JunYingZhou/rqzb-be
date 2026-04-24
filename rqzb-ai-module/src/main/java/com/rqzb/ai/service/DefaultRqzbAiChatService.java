package com.rqzb.ai.service;

import dev.langchain4j.model.chat.ChatModel;
import org.springframework.util.Assert;

public class DefaultRqzbAiChatService implements RqzbAiChatService {

    private final ChatModel chatModel;

    public DefaultRqzbAiChatService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public String chat(String userMessage) {
        Assert.hasText(userMessage, "userMessage must not be blank");
        return chatModel.chat(userMessage);
    }
}
