package com.birth.fit.service

import com.birth.fit.config.jwt.JwtTokenProvider
import com.birth.fit.domain.repository.UserRepository
import com.birth.fit.dto.JoinRequest
import com.birth.fit.dto.LoginRequest
import com.birth.fit.exception.error.AuthorizationException
import com.birth.fit.exception.error.LoginFailedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class UserService(
    @Autowired val userRepository: UserRepository,
    @Autowired val authenticationManager: AuthenticationManager,
    @Autowired val bCryptPasswordEncoder: BCryptPasswordEncoder,
    @Autowired val jwtTokenProvider: JwtTokenProvider
) {
    fun join(joinRequest: JoinRequest) {
        joinRequest.password = bCryptPasswordEncoder.encode(joinRequest.password)
        userRepository.save(joinRequest.getData(joinRequest))
    }

    fun login(loginRequest: LoginRequest): MutableMap<String, Any> {
        try{
            val username: String = loginRequest.email
            val password: String = loginRequest.password
            val authenticator = UsernamePasswordAuthenticationToken(username, password)

            authenticationManager.authenticate(authenticator)

            val accessToken: String = jwtTokenProvider.createAccessToken(username)
            val refreshToken: String = jwtTokenProvider.createRefreshToken(username)

            val model: MutableMap<String, Any> = HashMap()
            model["accessToken"] = accessToken
            model["refreshToken"] = refreshToken
            model["tokenType"] = "Bearer"

            return model
        }
        catch (e: Exception) {
            throw LoginFailedException("invalid email/password supplied")
        }
    }
}