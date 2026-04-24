package com.rqzb.system.controller;

import com.rqzb.ai.config.RqzbAiProperties;
import com.rqzb.ai.service.RqzbAiChatService;
import com.rqzb.ai.service.RqzbAiStreamingHandler;
import com.rqzb.common.ApiResponse;
import com.rqzb.common.service.GlobalThreadPoolService;
import com.rqzb.system.controller.request.AiChatRequest;
import com.rqzb.system.controller.response.AiChatResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api/system/ai")
public class SystemAiExampleController {

    private static final long SSE_TIMEOUT = 0L;

    private final ObjectProvider<RqzbAiChatService> aiChatServiceProvider;

    private final RqzbAiProperties aiProperties;

    private final GlobalThreadPoolService threadPoolService;

    public SystemAiExampleController(ObjectProvider<RqzbAiChatService> aiChatServiceProvider,
                                     RqzbAiProperties aiProperties,
                                     GlobalThreadPoolService threadPoolService) {
        this.aiChatServiceProvider = aiChatServiceProvider;
        this.aiProperties = aiProperties;
        this.threadPoolService = threadPoolService;
    }

    @PostMapping("/chat")
    public SseEmitter chat(@RequestBody(required = false) AiChatRequest request) {
        if (request == null || !StringUtils.hasText(request.getMessage())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message must not be blank");
        }

        RqzbAiChatService aiChatService = aiChatServiceProvider.getIfAvailable();
        if (aiChatService == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "AI module is disabled. Configure rqzb.ai.enabled=true and DASHSCOPE_API_KEY first.");
        }

        String message = request.getMessage().trim();
        String model = aiProperties.getChatModel();
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        AtomicBoolean completed = new AtomicBoolean(false);

        emitter.onTimeout(() -> {
            if (completed.compareAndSet(false, true)) {
                emitter.complete();
            }
        });
        emitter.onError(error -> completed.set(true));

        sendEvent(emitter, completed, "start", startPayload(model, message));

        threadPoolService.runAsync(() -> streamChat(aiChatService, message, model, emitter, completed));
        return emitter;
    }

    private static Map<String, String> startPayload(String model, String message) {
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("model", model);
        payload.put("message", message);
        return payload;
    }

    private static void streamChat(RqzbAiChatService aiChatService,
                                   String message,
                                   String model,
                                   SseEmitter emitter,
                                   AtomicBoolean completed) {
        try {
            aiChatService.chatStream(message, new RqzbAiStreamingHandler() {
                @Override
                public void onPartialResponse(String partialResponse) {
                    if (partialResponse == null || partialResponse.isEmpty()) {
                        return;
                    }
                    sendEvent(emitter, completed, "delta", new AiChatResponse(model, message, partialResponse));
                }

                @Override
                public void onCompleteResponse(String fullResponse) {
                    finish(emitter, completed, "complete",
                            new AiChatResponse(model, message, fullResponse == null ? "" : fullResponse));
                }

                @Override
                public void onError(Throwable throwable) {
                    finish(emitter, completed, "error", ApiResponse.fail(500, resolveErrorMessage(throwable)));
                }
            });
        } catch (RuntimeException ex) {
            finish(emitter, completed, "error", ApiResponse.fail(500, resolveErrorMessage(ex)));
        }
    }

    private static void sendEvent(SseEmitter emitter, AtomicBoolean completed, String eventName, Object data) {
        if (completed.get()) {
            return;
        }
        try {
            emitter.send(SseEmitter.event().name(eventName).data(data));
        } catch (IOException ex) {
            completed.set(true);
            emitter.completeWithError(ex);
        }
    }

    private static void finish(SseEmitter emitter, AtomicBoolean completed, String eventName, Object data) {
        if (!completed.compareAndSet(false, true)) {
            return;
        }
        try {
            emitter.send(SseEmitter.event().name(eventName).data(data));
        } catch (IOException ignored) {
            emitter.completeWithError(ignored);
            return;
        }
        emitter.complete();
    }

    private static String resolveErrorMessage(Throwable throwable) {
        if (throwable == null || !StringUtils.hasText(throwable.getMessage())) {
            return "AI streaming failed";
        }
        return throwable.getMessage();
    }
}
