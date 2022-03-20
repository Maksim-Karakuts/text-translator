package com.tinkoff.maksim.karakuts.text.translator;

import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {
    private static final Logger LOGGER =
        LoggerFactory.getLogger(AsyncConfiguration.class);

    @Bean
    public Executor taskExecutor() {
        LOGGER.debug("Creating async task executor for translation");
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("TranslationThread-");
        executor.initialize();
        return executor;
    }

}
