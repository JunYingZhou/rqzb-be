package com.rqzb.ai.config;

import com.rqzb.ai.service.RqzbAiChatService;
import dev.langchain4j.model.chat.ChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class RqzbAiConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(RqzbAiConfiguration.class);

    @Test
    void shouldNotCreateAiBeansWhenDisabled() {
        contextRunner.run(context -> {
            assertThat(context).doesNotHaveBean(ChatModel.class);
            assertThat(context).doesNotHaveBean(RqzbAiChatService.class);
        });
    }

    @Test
    void shouldCreateAiBeansWhenEnabled() {
        contextRunner
                .withPropertyValues(
                        "rqzb.ai.enabled=true",
                        "rqzb.ai.api-key=test-key",
                        "rqzb.ai.chat-model=gpt-4o-mini",
                        "rqzb.ai.base-url=https://api.openai.com/v1",
                        "rqzb.ai.timeout=15s")
                .run(context -> {
                    assertThat(context).hasSingleBean(ChatModel.class);
                    assertThat(context).hasSingleBean(RqzbAiChatService.class);
                });
    }

    @Test
    void shouldFailFastWhenApiKeyMissing() {
        contextRunner
                .withPropertyValues(
                        "rqzb.ai.enabled=true",
                        "rqzb.ai.chat-model=gpt-4o-mini")
                .run(context -> {
                    assertThat(context).hasFailed();
                    assertThat(context.getStartupFailure())
                            .hasMessageContaining("rqzb.ai.api-key must not be blank");
                });
    }
}
