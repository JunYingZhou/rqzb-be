package com.rqzb.ai.service;

public interface RqzbAiStreamingHandler {

    default void onPartialResponse(String partialResponse) {
    }

    default void onCompleteResponse(String fullResponse) {
    }

    default void onError(Throwable throwable) {
    }
}
