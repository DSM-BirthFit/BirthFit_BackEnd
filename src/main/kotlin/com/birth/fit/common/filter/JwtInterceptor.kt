package com.birth.fit.common.filter

import com.amazonaws.services.securitytoken.model.ExpiredTokenException
import com.birth.fit.common.util.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
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
            refreshToken != null && jwtTokenProvider.validateToken(refreshToken)
        }
    }
}