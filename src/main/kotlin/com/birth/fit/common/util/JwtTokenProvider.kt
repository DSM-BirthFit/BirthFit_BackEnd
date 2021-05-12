package com.birth.fit.common.util

import com.birth.fit.common.exception.error.ExpiredTokenException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*
import javax.servlet.http.HttpServletRequest


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

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken: String? = req.getHeader("Authorization")
        bearerToken?: return null

        if(!bearerToken.startsWith("Bearer ")) return null

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

    fun getToken(header: String): String? {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val token = request.getHeader(header)
        if(!this.validateToken(token.substring(7))) throw ExpiredTokenException("토큰이 만료되었습니다.")
        return token.substring(7)
    }
}