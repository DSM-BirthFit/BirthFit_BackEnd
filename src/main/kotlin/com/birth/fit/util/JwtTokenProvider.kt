package com.birth.fit.util

import com.birth.fit.exception.error.InvalidTokenException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${auth.jwt.secret}")
    private val secretKey: String,

    @Value("\${auth.jwt.exp.access}")
    private val accessTokenExpiration: Long,

    @Value("\${auth.jwt.exp.refresh}")
    private val refreshTokenExpiration: Long,

    @Value("\${auth.jwt.header}")
    private val header: String,

    @Value("\${auth.jwt.prefix}")
    private val prefix: String
) {

    fun createAccessToken(username: String): String {
        val claims: Claims = Jwts.claims().setSubject(username)
        claims["type"] = "access_token"

        val validity = Date(Date().time + accessTokenExpiration)

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

        val validity = Date(Date().time + refreshTokenExpiration)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    fun getUsername(token: String): String {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body.subject
    }

    fun resolveToken(bearerToken: String?): String? {
        bearerToken?: throw InvalidTokenException("Token does not exist.")

        if(!bearerToken.startsWith(prefix)) throw InvalidTokenException("Token type is not a bearer.")

        return bearerToken.substring(7, bearerToken.length)
    }

    fun validateToken(token: String): Boolean {
        try{
            val claims: Jws<Claims> = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            if(claims.body.expiration.before(Date())) return false
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun getType(token: String): Any? {
        val claims: Jws<Claims> = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
        return claims.body["type"]
    }
}