package com.birth.fit.config.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(
    @Qualifier("authDetailsService")
    @Autowired private val authDetailsService: AuthDetailsService
) {
    @Value("\${auth.jwt.secret}")
    private lateinit var secretKey: String

    @Value("\${auth.jwt.exp.access}")
    private val accessTokenExpiration: Long? = null

    @Value("\${auth.jwt.exp.refresh}")
    private val refreshTokenExpiration: Long? = null

    @Value("\${auth.jwt.header}")
    private lateinit var header: String

    @Value("\${auth.jwt.prefix}")
    private lateinit var prefix: String

    fun createAccessToken(username: String): String {
        val claims: Claims = Jwts.claims().setSubject(username)
        claims["type"] = "access_token"

        val validity = Date(Date().time + accessTokenExpiration!!)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    fun createRefreshToken(username: String): String {
        val claims: Claims = Jwts.claims().setSubject(username)
        claims["type"] = "refresh_token"

        val validity = Date(Date().time + refreshTokenExpiration!!)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val authDetails : AuthDetails = authDetailsService.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(authDetails, "", authDetails.authorities)
    }

    fun getUsername(token: String): String {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body.subject
    }

    fun resolveToken(req : HttpServletRequest): String? {
        val bearerToken: String? = req.getHeader(header)
        bearerToken?: return null

        if(!bearerToken.startsWith(prefix)) return null

        return bearerToken.substring(7, bearerToken.length)
    }

    fun validateToken(token: String): Boolean {
        try{
            val claims: Jws<Claims> = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            if(claims.body.expiration.before(Date())) return false
            return true
        } catch (e: Exception) {
            throw Exception()
        }
    }
}