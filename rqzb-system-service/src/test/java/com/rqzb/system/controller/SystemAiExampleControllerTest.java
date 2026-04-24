package com.rqzb.system.controller;

import com.rqzb.ai.config.RqzbAiProperties;
import com.rqzb.ai.service.RqzbAiChatService;
import com.rqzb.ai.service.RqzbAiStreamingHandler;
import com.rqzb.common.ApiResponse;
import com.rqzb.system.controller.request.AiChatRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SystemAiExampleControllerTest {

    @Test
    void shouldReturn400WhenMessageBlank() {
        SystemAiExampleController controller = new SystemAiExampleController(mockProvider(null), aiProperties());

        ResponseEntity<?> response = controller.chat(new AiChatRequest("   "));

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isInstanceOf(ApiResponse.class);
        ApiResponse<?> body = (ApiResponse<?>) response.getBody();
        assertThat(body.getCode()).isEqualTo(400);
        assertThat(body.getMessage()).isEqualTo("message must not be blank");
    }

    @Test
    void shouldReturn503WhenAiDisabled() {
        SystemAiExampleController controller = new SystemAiExampleController(mockProvider(null), aiProperties());

        ResponseEntity<?> response = controller.chat(new AiChatRequest("hello"));

        assertThat(response.getStatusCode().value()).isEqualTo(503);
        assertThat(response.getBody()).isInstanceOf(ApiResponse.class);
        ApiResponse<?> body = (ApiResponse<?>) response.getBody();
        assertThat(body.getCode()).isEqualTo(503);
        assertThat(body.getMessage()).contains("DASHSCOPE_API_KEY");
    }

    @Test
    void shouldReturnSseEmitterWhenAiEnabled() {
        RqzbAiChatService aiChatService = mock(RqzbAiChatService.class);
        doAnswer(invocation -> {
            RqzbAiStreamingHandler streamingHandler = invocation.getArgument(1);
            streamingHandler.onPartialResponse("This is a Spring Cloud ");
            streamingHandler.onCompleteResponse("This is a Spring Cloud multi-module backend system.");
            return null;
        }).when(aiChatService).chatStream(eq("Introduce this system briefly."), any(RqzbAiStreamingHandler.class));

        SystemAiExampleController controller = new SystemAiExampleController(mockProvider(aiChatService), aiProperties());

        ResponseEntity<?> response = controller.chat(new AiChatRequest("Introduce this system briefly."));

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.TEXT_EVENT_STREAM);
        assertThat(response.getBody()).isInstanceOf(SseEmitter.class);
        verify(aiChatService).chatStream(eq("Introduce this system briefly."), any(RqzbAiStreamingHandler.class));
    }

    @SuppressWarnings("unchecked")
    private static ObjectProvider<RqzbAiChatService> mockProvider(RqzbAiChatService aiChatService) {
        ObjectProvider<RqzbAiChatService> provider = mock(ObjectProvider.class);
        when(provider.getIfAvailable()).thenReturn(aiChatService);
        return provider;
    }

    private static RqzbAiProperties aiProperties() {
        RqzbAiProperties properties = new RqzbAiProperties();
        properties.setChatModel("qwen-plus");
        return properties;
    }
}
