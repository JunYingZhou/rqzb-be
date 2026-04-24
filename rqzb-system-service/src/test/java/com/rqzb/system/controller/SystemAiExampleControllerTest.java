package com.rqzb.system.controller;

import com.rqzb.ai.config.RqzbAiProperties;
import com.rqzb.ai.service.RqzbAiChatService;
import com.rqzb.ai.service.RqzbAiStreamingHandler;
import com.rqzb.common.service.GlobalThreadPoolService;
import com.rqzb.system.exception.SystemExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SystemAiExampleControllerTest {

    @Test
    void shouldReturn400WhenMessageBlank() throws Exception {
        MockMvc mockMvc = mockMvc(null);

        mockMvc.perform(post("/api/system/ai/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"   \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("\"code\":400")))
                .andExpect(content().string(containsString("\"message\":\"message must not be blank\"")));
    }

    @Test
    void shouldReturn503WhenAiDisabled() throws Exception {
        MockMvc mockMvc = mockMvc(null);

        mockMvc.perform(post("/api/system/ai/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"hello\"}"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("\"code\":503")))
                .andExpect(content().string(containsString("DASHSCOPE_API_KEY")));
    }

    @Test
    void shouldStreamSseWithoutConverterError() throws Exception {
        RqzbAiChatService aiChatService = mock(RqzbAiChatService.class);
        doAnswer(invocation -> {
            RqzbAiStreamingHandler streamingHandler = invocation.getArgument(1);
            streamingHandler.onPartialResponse("This is a Spring Cloud ");
            streamingHandler.onCompleteResponse("This is a Spring Cloud multi-module backend system.");
            return null;
        }).when(aiChatService).chatStream(eq("Introduce this system briefly."), any(RqzbAiStreamingHandler.class));

        MockMvc mockMvc = mockMvc(aiChatService);

        MvcResult mvcResult = mockMvc.perform(post("/api/system/ai/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"Introduce this system briefly.\"}"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM))
                .andExpect(content().string(containsString("event:start")))
                .andExpect(content().string(containsString("event:delta")))
                .andExpect(content().string(containsString("event:complete")))
                .andExpect(content().string(containsString("This is a Spring Cloud multi-module backend system.")));
    }

    @SuppressWarnings("unchecked")
    private static MockMvc mockMvc(RqzbAiChatService aiChatService) {
        ObjectProvider<RqzbAiChatService> provider = mock(ObjectProvider.class);
        when(provider.getIfAvailable()).thenReturn(aiChatService);

        GlobalThreadPoolService threadPoolService = mock(GlobalThreadPoolService.class);
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return CompletableFuture.completedFuture(null);
        }).when(threadPoolService).runAsync(any(Runnable.class));

        SystemAiExampleController controller = new SystemAiExampleController(provider, aiProperties(), threadPoolService);
        return MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new SystemExceptionHandler())
                .build();
    }

    private static RqzbAiProperties aiProperties() {
        RqzbAiProperties properties = new RqzbAiProperties();
        properties.setChatModel("qwen-plus");
        return properties;
    }
}
