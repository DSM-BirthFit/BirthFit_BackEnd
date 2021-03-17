package com.birth.fit.config

import com.birth.fit.config.jwt.AuthDetailsService
import com.birth.fit.config.jwt.JwtConfig
import com.birth.fit.config.jwt.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Autowired private val authDetailsService: AuthDetailsService,
    @Autowired private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    @Autowired private val jwtTokenProvider: JwtTokenProvider
) : WebSecurityConfigurerAdapter() {

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/email/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .apply(JwtConfig(jwtTokenProvider))
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .userDetailsService(authDetailsService)
            .passwordEncoder(bCryptPasswordEncoder)
    }
}