package com.rqzb.system.controller;

import com.rqzb.ai.config.RqzbAiProperties;
import com.rqzb.ai.service.RqzbAiChatService;
import com.rqzb.ai.service.RqzbAiStreamingHandler;
import com.rqzb.common.ApiResponse;
import com.rqzb.system.controller.request.AiChatRequest;
import com.rqzb.system.controller.response.AiChatResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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

    public SystemAiExampleController(ObjectProvider<RqzbAiChatService> aiChatServiceProvider,
                                     RqzbAiProperties aiProperties) {
        this.aiChatServiceProvider = aiChatServiceProvider;
        this.aiProperties = aiProperties;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody(required = false) AiChatRequest request) {
        if (request == null || !StringUtils.hasText(request.getMessage())) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(400, "message must not be blank"));
        }

        RqzbAiChatService aiChatService = aiChatServiceProvider.getIfAvailable();
        if (aiChatService == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ApiResponse.fail(503,
                            "AI module is disabled. Configure rqzb.ai.enabled=true and DASHSCOPE_API_KEY first."));
        }

        String message = request.getMessage().trim();
        String model = aiProperties.getChatModel();
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        AtomicBoolean completed = new AtomicBoolean(false);

        emitter.onTimeout(emitter::complete);
        emitter.onError(error -> completed.set(true));

        sendEventOrComplete(emitter, completed, "start", startPayload(model, message));

        try {
            aiChatService.chatStream(message, new RqzbAiStreamingHandler() {
                @Override
                public void onPartialResponse(String partialResponse) {
                    if (partialResponse == null || partialResponse.isEmpty()) {
                        return;
                    }
                    sendEventOrComplete(emitter, completed, "delta",
                            new AiChatResponse(model, message, partialResponse));
                }

                @Override
                public void onCompleteResponse(String fullResponse) {
                    if (completed.compareAndSet(false, true)) {
                        sendEventOrComplete(emitter, completed, "complete",
                                new AiChatResponse(model, message, fullResponse == null ? "" : fullResponse));
                        emitter.complete();
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    if (completed.compareAndSet(false, true)) {
                        sendEventQuietly(emitter, "error", ApiResponse.fail(500, resolveErrorMessage(throwable)));
                        emitter.complete();
                    }
                }
            });
        } catch (RuntimeException ex) {
            if (completed.compareAndSet(false, true)) {
                sendEventQuietly(emitter, "error", ApiResponse.fail(500, resolveErrorMessage(ex)));
                emitter.complete();
            }
        }

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(emitter);
    }

    private static Map<String, String> startPayload(String model, String message) {
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("model", model);
        payload.put("message", message);
        return payload;
    }

    private static void sendEventOrComplete(SseEmitter emitter, AtomicBoolean completed, String eventName, Object data) {
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

    private static void sendEventQuietly(SseEmitter emitter, String eventName, Object data) {
        try {
            emitter.send(SseEmitter.event().name(eventName).data(data));
        } catch (IOException ignored) {
            emitter.complete();
        }
    }

    private static String resolveErrorMessage(Throwable throwable) {
        if (throwable == null || !StringUtils.hasText(throwable.getMessage())) {
            return "AI streaming failed";
        }
        return throwable.getMessage();
    }
}
