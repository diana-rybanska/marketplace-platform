package com.marketplace.common.errors.config;

import com.marketplace.common.errors.GlobalExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class MarketplaceErrorsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler globalExceptionHandler(ObjectProvider<MessageSource> provider) {
        return new GlobalExceptionHandler(provider.getIfAvailable());
    }
}
