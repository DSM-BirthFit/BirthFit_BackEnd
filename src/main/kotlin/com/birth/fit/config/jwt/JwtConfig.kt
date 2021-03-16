package com.birth.fit.config.jwt

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtConfig(
    private val jwtTokenProvider: JwtTokenProvider
): SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
    override fun configure(builder: HttpSecurity?) {
        val customFilter = JwtTokenFilter(jwtTokenProvider)
        builder?.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}