package com.birth.fit.common.filter

import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CorsFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "*")
        response.addHeader("Access-Control-Allow-Headers", "*")
        response.addHeader("Access-Control-Expose-Headers", "*")
        response.addHeader("Access-Control-Allow-Credentials", "true")
        response.addIntHeader("Access-Control-Max-Age", 10)
        filterChain.doFilter(request, response)
    }
}