package net.sublime.rest.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class JwtUsernameAndPasswordAuthenticationFilter(
    private var authenticationManagerImpl: AuthenticationManager
) : UsernamePasswordAuthenticationFilter() {
    override fun attemptAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ): Authentication {

        try {
            val authenticationRequest = ObjectMapper().readValue(
                request?.inputStream,
                UsernameAndPasswordAuthenticationRequest::class.java
            )

            val authentication: Authentication = UsernamePasswordAuthenticationToken(
                authenticationRequest.username,
                authenticationRequest.password
            )

            return authenticationManagerImpl.authenticate(authentication)
        } catch (i: IOException) {
            throw RuntimeException()
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val token = Jwts.builder()
            .setSubject(authResult.name)
            .claim("authorities", authResult.authorities)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.toByteArray()))
            .compact()
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(
            "{\"token\" : \"Bearer $token\"}"
        )
        response.addHeader(AUTHORIZATION_HEADER, "Bearer $token")
    }
}
