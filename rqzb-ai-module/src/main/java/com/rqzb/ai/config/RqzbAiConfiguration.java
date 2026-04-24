package com.rqzb.ai.config;

import com.rqzb.ai.service.DefaultRqzbAiChatService;
import com.rqzb.ai.service.RqzbAiChatService;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Configuration
@EnableConfigurationProperties(RqzbAiProperties.class)
public class RqzbAiConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "rqzb.ai", name = "enabled", havingValue = "true")
    @ConditionalOnMissingBean(ChatModel.class)
    public ChatModel rqzbChatModel(RqzbAiProperties properties) {
        validateProperties(properties);
        OpenAiChatModel.OpenAiChatModelBuilder builder = OpenAiChatModel.builder()
                .apiKey(properties.getApiKey())
                .modelName(properties.getChatModel())
                .timeout(properties.getTimeout())
                .logRequests(properties.isLogRequests())
                .logResponses(properties.isLogResponses());

        if (StringUtils.hasText(properties.getBaseUrl())) {
            builder.baseUrl(properties.getBaseUrl());
        }

        return builder.build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "rqzb.ai", name = "enabled", havingValue = "true")
    @ConditionalOnMissingBean(StreamingChatModel.class)
    public StreamingChatModel rqzbStreamingChatModel(RqzbAiProperties properties) {
        validateProperties(properties);
        OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder builder = OpenAiStreamingChatModel.builder()
                .apiKey(properties.getApiKey())
                .modelName(properties.getChatModel())
                .timeout(properties.getTimeout())
                .logRequests(properties.isLogRequests())
                .logResponses(properties.isLogResponses());

        if (StringUtils.hasText(properties.getBaseUrl())) {
            builder.baseUrl(properties.getBaseUrl());
        }

        return builder.build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "rqzb.ai", name = "enabled", havingValue = "true")
    @ConditionalOnMissingBean(RqzbAiChatService.class)
    public RqzbAiChatService rqzbAiChatService(ObjectProvider<ChatModel> chatModelProvider,
                                              ObjectProvider<StreamingChatModel> streamingChatModelProvider) {
        return new DefaultRqzbAiChatService(
                chatModelProvider.getIfAvailable(),
                streamingChatModelProvider.getIfAvailable());
    }

    private static void validateProperties(RqzbAiProperties properties) {
        Assert.hasText(properties.getApiKey(), "rqzb.ai.api-key must not be blank when rqzb.ai.enabled=true");
        Assert.hasText(properties.getChatModel(), "rqzb.ai.chat-model must not be blank when rqzb.ai.enabled=true");
        Assert.notNull(properties.getTimeout(), "rqzb.ai.timeout must not be null when rqzb.ai.enabled=true");
    }
}
