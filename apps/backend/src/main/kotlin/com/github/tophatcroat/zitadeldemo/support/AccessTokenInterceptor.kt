package com.github.tophatcroat.zitadeldemo.support

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import java.io.IOException


class AccessTokenInterceptor(
    private val tokenAccessor: TokenAccessor
) : ClientHttpRequestInterceptor {

    @Throws(IOException::class)
    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        val accessToken = tokenAccessor.getAccessTokenForCurrentUser()

        if (accessToken != null) {
            request.headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.tokenValue)
        }

        return execution.execute(request, body)
    }
}