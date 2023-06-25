package com.github.tophatcroat.zitadeldemo.support

import mu.KotlinLogging
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class TokenAccessor(
    private val authorizedClientService: OAuth2AuthorizedClientService
) {

    fun getAccessTokenForCurrentUser() = getAccessToken(SecurityContextHolder.getContext().authentication)

    private final fun getAccessToken(auth: Authentication): OAuth2AccessToken? {
        logger.debug("Get AccessToken for current user {}: begin", auth.getName())
        val authToken = auth as OAuth2AuthenticationToken
        val clientId = authToken.authorizedClientRegistrationId
        val username = auth.getName()
        val oauth2Client = authorizedClientService.loadAuthorizedClient<OAuth2AuthorizedClient>(clientId, username)

        if (oauth2Client == null) {
            logger.warn("Get AccessToken for current user failed: client not found")
            return null
        }

        val accessToken = oauth2Client.accessToken
        logger.debug("Get AccessToken for current user {}: end", auth.getName())
        return accessToken
    }
}