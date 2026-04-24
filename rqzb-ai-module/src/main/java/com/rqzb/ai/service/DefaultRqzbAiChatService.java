package com.rqzb.ai.service;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import org.springframework.util.Assert;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DefaultRqzbAiChatService implements RqzbAiChatService {

    private final ChatModel chatModel;

    private final StreamingChatModel streamingChatModel;

    public DefaultRqzbAiChatService(ChatModel chatModel, StreamingChatModel streamingChatModel) {
        Assert.isTrue(chatModel != null || streamingChatModel != null,
                "At least one AI chat model must be configured");
        this.chatModel = chatModel;
        this.streamingChatModel = streamingChatModel;
    }

    @Override
    public String chat(String userMessage) {
        String normalizedUserMessage = normalizeUserMessage(userMessage);
        if (chatModel != null) {
            return chatModel.chat(normalizedUserMessage);
        }
        return aggregateStreamingResponse(normalizedUserMessage);
    }

    @Override
    public void chatStream(String userMessage, RqzbAiStreamingHandler streamingHandler) {
        String normalizedUserMessage = normalizeUserMessage(userMessage);
        Assert.notNull(streamingHandler, "streamingHandler must not be null");

        if (streamingChatModel != null) {
            StringBuilder fullResponse = new StringBuilder();
            streamingChatModel.chat(normalizedUserMessage, new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String partialResponse) {
                    if (partialResponse != null) {
                        fullResponse.append(partialResponse);
                        streamingHandler.onPartialResponse(partialResponse);
                    }
                }

                @Override
                public void onCompleteResponse(ChatResponse chatResponse) {
                    String completedResponse = fullResponse.length() > 0
                            ? fullResponse.toString()
                            : extractText(chatResponse);
                    streamingHandler.onCompleteResponse(completedResponse);
                }

                @Override
                public void onError(Throwable throwable) {
                    streamingHandler.onError(throwable);
                }
            });
            return;
        }

        Assert.state(chatModel != null, "No AI chat model available");
        try {
            String answer = chatModel.chat(normalizedUserMessage);
            streamingHandler.onPartialResponse(answer);
            streamingHandler.onCompleteResponse(answer);
        } catch (Throwable throwable) {
            streamingHandler.onError(throwable);
        }
    }

    private String aggregateStreamingResponse(String userMessage) {
        CompletableFuture<String> future = new CompletableFuture<>();

        chatStream(userMessage, new RqzbAiStreamingHandler() {
            @Override
            public void onCompleteResponse(String fullResponse) {
                future.complete(fullResponse == null ? "" : fullResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        });

        try {
            return future.get();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("AI streaming was interrupted", ex);
        } catch (ExecutionException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new IllegalStateException("AI streaming failed", cause);
        }
    }

    private static String normalizeUserMessage(String userMessage) {
        Assert.hasText(userMessage, "userMessage must not be blank");
        return userMessage.trim();
    }

    private static String extractText(ChatResponse chatResponse) {
        if (chatResponse == null || chatResponse.aiMessage() == null || chatResponse.aiMessage().text() == null) {
            return "";
        }
        return chatResponse.aiMessage().text();
    }
}
