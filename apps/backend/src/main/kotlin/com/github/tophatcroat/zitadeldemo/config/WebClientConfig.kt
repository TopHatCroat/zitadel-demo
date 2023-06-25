package com.github.tophatcroat.zitadeldemo.config

import com.github.tophatcroat.zitadeldemo.support.AccessTokenInterceptor
import com.github.tophatcroat.zitadeldemo.support.TokenAccessor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@Configuration
class WebClientConfig(
    private val tokenAccessor: TokenAccessor
) {

    @Bean
    @Qualifier("zitadel")
    fun restTemplate(): RestTemplate {
        return RestTemplateBuilder()
            .interceptors(AccessTokenInterceptor(tokenAccessor))
            .build()
    }
}