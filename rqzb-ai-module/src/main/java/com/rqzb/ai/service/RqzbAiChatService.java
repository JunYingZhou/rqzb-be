package com.rqzb.ai.service;

public interface RqzbAiChatService {

    String chat(String userMessage);

    void chatStream(String userMessage, RqzbAiStreamingHandler streamingHandler);
}
