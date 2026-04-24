package com.rqzb.ai.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultRqzbAiChatServiceTest {

    @Test
    void shouldUseSyncChatModelWhenAvailable() {
        ChatModel chatModel = mock(ChatModel.class);
        when(chatModel.chat("hello")).thenReturn("world");

        DefaultRqzbAiChatService service = new DefaultRqzbAiChatService(chatModel, null);

        assertThat(service.chat(" hello ")).isEqualTo("world");
        verify(chatModel).chat("hello");
    }

    @Test
    void shouldAggregateStreamingResponseWhenSyncModelMissing() {
        StreamingChatModel streamingChatModel = mock(StreamingChatModel.class);
        doAnswer(invocation -> {
            StreamingChatResponseHandler handler = invocation.getArgument(1);
            handler.onPartialResponse("Hello");
            handler.onPartialResponse(" world");
            handler.onCompleteResponse(ChatResponse.builder()
                    .aiMessage(AiMessage.aiMessage("Hello world"))
                    .build());
            return null;
        }).when(streamingChatModel).chat(eq("hello"), any(StreamingChatResponseHandler.class));

        DefaultRqzbAiChatService service = new DefaultRqzbAiChatService(null, streamingChatModel);

        assertThat(service.chat(" hello ")).isEqualTo("Hello world");
    }

    @Test
    void shouldForwardStreamingChunksToHandler() {
        StreamingChatModel streamingChatModel = mock(StreamingChatModel.class);
        doAnswer(invocation -> {
            StreamingChatResponseHandler handler = invocation.getArgument(1);
            handler.onPartialResponse("multi");
            handler.onPartialResponse("-module");
            handler.onCompleteResponse(ChatResponse.builder()
                    .aiMessage(AiMessage.aiMessage("multi-module"))
                    .build());
            return null;
        }).when(streamingChatModel).chat(eq("rqzb"), any(StreamingChatResponseHandler.class));

        DefaultRqzbAiChatService service = new DefaultRqzbAiChatService(null, streamingChatModel);
        List<String> chunks = new ArrayList<>();
        AtomicReference<String> completed = new AtomicReference<>();

        service.chatStream(" rqzb ", new RqzbAiStreamingHandler() {
            @Override
            public void onPartialResponse(String partialResponse) {
                chunks.add(partialResponse);
            }

            @Override
            public void onCompleteResponse(String fullResponse) {
                completed.set(fullResponse);
            }
        });

        assertThat(chunks).containsExactly("multi", "-module");
        assertThat(completed.get()).isEqualTo("multi-module");
    }
}
