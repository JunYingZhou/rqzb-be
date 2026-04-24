package com.rqzb.system.controller;

import com.rqzb.ai.config.RqzbAiProperties;
import com.rqzb.ai.service.RqzbAiChatService;
import com.rqzb.common.ApiResponse;
import com.rqzb.system.controller.request.AiChatRequest;
import com.rqzb.system.controller.response.AiChatResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/ai")
public class SystemAiExampleController {

    private final ObjectProvider<RqzbAiChatService> aiChatServiceProvider;

    private final RqzbAiProperties aiProperties;

    public SystemAiExampleController(ObjectProvider<RqzbAiChatService> aiChatServiceProvider,
                                     RqzbAiProperties aiProperties) {
        this.aiChatServiceProvider = aiChatServiceProvider;
        this.aiProperties = aiProperties;
    }

    @PostMapping("/chat")
    public ApiResponse<AiChatResponse> chat(@RequestBody(required = false) AiChatRequest request) {
        if (request == null || !StringUtils.hasText(request.getMessage())) {
            return ApiResponse.fail(400, "message must not be blank");
        }

        RqzbAiChatService aiChatService = aiChatServiceProvider.getIfAvailable();
        if (aiChatService == null) {
            return ApiResponse.fail(503, "AI module is disabled. Configure rqzb.ai.enabled=true and DASHSCOPE_API_KEY first.");
        }

        String message = request.getMessage().trim();
        String answer = aiChatService.chat(message);
        AiChatResponse response = new AiChatResponse(aiProperties.getChatModel(), message, answer);
        return ApiResponse.ok(response);
    }
}
