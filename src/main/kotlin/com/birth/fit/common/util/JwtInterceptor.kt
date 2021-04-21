package com.birth.fit.common.util

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
        if (handler !is HandlerMethod) { return true; }

        val token: String? = jwtTokenProvider.resolveToken(request)
        if(token != null && jwtTokenProvider.validateToken(token)) {
            return true
        } else {
            val refreshToken: String? = request.getHeader("X-RefreshToken")?.substring(7)
            if(refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                return true
            }
        }

        return false
    }
}