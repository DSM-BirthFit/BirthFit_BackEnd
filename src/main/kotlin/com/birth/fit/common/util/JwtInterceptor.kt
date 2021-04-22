package com.birth.fit.common.util

import com.birth.fit.common.exception.error.InvalidTokenException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtInterceptor(
    @Autowired private val jwtTokenProvider: JwtTokenProvider
): HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val token: String? = jwtTokenProvider.resolveToken(request)
        return if(token != null && jwtTokenProvider.validateToken(token)) {
            true
        } else {
            val refreshToken: String? = request.getHeader("X-Refresh-Token")?.substring(7)
            if(refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                true
            } else {
                throw InvalidTokenException("Token is invalid.")
            }
        }
    }
}