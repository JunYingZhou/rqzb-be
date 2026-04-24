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
        assertThat(response.getMessage()).isEqualTo("message不能为空");
    }

    @Test
    void shouldReturn503WhenAiDisabled() {
        SystemAiExampleController controller = new SystemAiExampleController(mockProvider(null), aiProperties());

        ApiResponse<AiChatResponse> response = controller.chat(new AiChatRequest("你好"));

        assertThat(response.getCode()).isEqualTo(503);
        assertThat(response.getMessage()).contains("AI模块未启用");
    }

    @Test
    void shouldReturnAnswerWhenAiEnabled() {
        RqzbAiChatService aiChatService = mock(RqzbAiChatService.class);
        when(aiChatService.chat("介绍一下这个系统")).thenReturn("这是一个基于 Spring Cloud 的多模块后端系统。");

        SystemAiExampleController controller = new SystemAiExampleController(mockProvider(aiChatService), aiProperties());

        ApiResponse<AiChatResponse> response = controller.chat(new AiChatRequest("介绍一下这个系统"));

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getModel()).isEqualTo("gpt-4o-mini");
        assertThat(response.getData().getMessage()).isEqualTo("介绍一下这个系统");
        assertThat(response.getData().getAnswer()).isEqualTo("这是一个基于 Spring Cloud 的多模块后端系统。");
    }

    @SuppressWarnings("unchecked")
    private static ObjectProvider<RqzbAiChatService> mockProvider(RqzbAiChatService aiChatService) {
        ObjectProvider<RqzbAiChatService> provider = mock(ObjectProvider.class);
        when(provider.getIfAvailable()).thenReturn(aiChatService);
        return provider;
    }

    private static RqzbAiProperties aiProperties() {
        RqzbAiProperties properties = new RqzbAiProperties();
        properties.setChatModel("gpt-4o-mini");
        return properties;
    }
}
