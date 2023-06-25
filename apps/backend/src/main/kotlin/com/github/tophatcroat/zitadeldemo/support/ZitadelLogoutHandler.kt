package com.github.tophatcroat.zitadeldemo.support

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Component
import java.io.IOException
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@Component
class ZitadelLogoutHandler : LogoutHandler {
    override fun logout(request: HttpServletRequest, response: HttpServletResponse, auth: Authentication) {
        val principal = auth.getPrincipal() as DefaultOidcUser
        val idToken = principal.idToken
        logger.debug("Propagate logout to zitadel for user. userId={}", idToken.subject)
        val idTokenValue = idToken.tokenValue
        val defaultRedirectUri = generateAppUri(request)
        val logoutUrl = createZitadelLogoutUrl(idTokenValue, defaultRedirectUri)
        try {
            response.sendRedirect(logoutUrl)
        } catch (e: IOException) {
            logger.error("Could not propagate logout for user to zitadel. userId={}", idToken.subject, e)
        }
    }

    private fun generateAppUri(request: HttpServletRequest): String {
        var hostname = request.serverName + ":" + request.serverPort
        val isStandardHttps = "https" == request.scheme && request.serverPort == 443
        val isStandardHttp = "http" == request.scheme && request.serverPort == 80
        if (isStandardHttps || isStandardHttp) {
            hostname = request.serverName
        }
        return request.scheme + "://" + hostname + request.contextPath
    }

    private fun createZitadelLogoutUrl(idTokenValue: String, postRedirectUri: String): String {
        return (ZITADEL_END_SESSION_ENDPOINT +  //
                "?id_token_hint=" + idTokenValue //
                + "&post_logout_redirect_uri=" + postRedirectUri)
    }

    companion object {
        const val ZITADEL_END_SESSION_ENDPOINT = "https://accounts.zitadel.ch/oauth/v2/endsession"
    }
}