package com.github.tophatcroat.zitadeldemo.web

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api")
internal class ApiController(
    @Qualifier("zitadel") private val restTemplate: RestTemplate
) {

    @GetMapping("/greetings/me")
    fun greetme(auth: Authentication?): Map<*, *>? {
        logger.debug("Calling backend API: begin")
        val payload: Map<*, *>? = restTemplate.getForObject(
            "http://localhost:18090/api/greet/me",
            Map::class.java
        )

        logger.debug("Calling backend API: end")
        return payload
    }
}