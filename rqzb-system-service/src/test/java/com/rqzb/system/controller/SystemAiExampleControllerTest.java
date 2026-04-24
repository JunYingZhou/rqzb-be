package com.rqzb.system.controller;

import com.rqzb.ai.config.RqzbAiProperties;
import com.rqzb.ai.service.RqzbAiChatService;
import com.rqzb.common.ApiResponse;
import com.rqzb.system.controller.request.AiChatRequest;
import com.rqzb.system.controller.response.AiChatResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SystemAiExampleControllerTest {

    @Test
    void shouldReturn400WhenMessageBlank() {
        SystemAiExampleController controller = new SystemAiExampleController(mockProvider(null), aiProperties());

        ApiResponse<AiChatResponse> response = controller.chat(new AiChatRequest("   "));

        assertThat(response.getCode()).isEqualTo(400);
        assertThat(response.getMessage()).isEqualTo("message must not be blank");
    }

    @Test
    void shouldReturn503WhenAiDisabled() {
        SystemAiExampleController controller = new SystemAiExampleController(mockProvider(null), aiProperties());

        ApiResponse<AiChatResponse> response = controller.chat(new AiChatRequest("hello"));

        assertThat(response.getCode()).isEqualTo(503);
        assertThat(response.getMessage()).contains("DASHSCOPE_API_KEY");
    }

    @Test
    void shouldReturnAnswerWhenAiEnabled() {
        RqzbAiChatService aiChatService = mock(RqzbAiChatService.class);
        when(aiChatService.chat("Introduce this system briefly."))
                .thenReturn("This is a Spring Cloud multi-module backend system.");

        SystemAiExampleController controller = new SystemAiExampleController(mockProvider(aiChatService), aiProperties());

        ApiResponse<AiChatResponse> response = controller.chat(new AiChatRequest("Introduce this system briefly."));

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getModel()).isEqualTo("qwen-plus");
        assertThat(response.getData().getMessage()).isEqualTo("Introduce this system briefly.");
        assertThat(response.getData().getAnswer()).isEqualTo("This is a Spring Cloud multi-module backend system.");
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
