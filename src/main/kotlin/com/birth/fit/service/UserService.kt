package com.birth.fit.service

import com.birth.fit.config.jwt.JwtTokenProvider
import com.birth.fit.domain.entity.Email
import com.birth.fit.domain.repository.EmailRepository
import com.birth.fit.domain.repository.UserRepository
import com.birth.fit.dto.JoinRequest
import com.birth.fit.dto.LoginRequest
import com.birth.fit.dto.TokenResponse
import com.birth.fit.exception.error.InvalidAuthEmailException
import com.birth.fit.exception.error.LoginFailedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired val userRepository: UserRepository,
    @Autowired val emailRepository: EmailRepository,
    @Autowired val authenticationManager: AuthenticationManager,
    @Autowired val bCryptPasswordEncoder: BCryptPasswordEncoder,
    @Autowired val jwtTokenProvider: JwtTokenProvider
) {
    fun join(joinRequest: JoinRequest) {
        emailRepository.findById(joinRequest.email)
            .filter(Email::isVerified)
            .orElseThrow {
                InvalidAuthEmailException("This email is not authenticated.")
            }

        joinRequest.password = bCryptPasswordEncoder.encode(joinRequest.password)
        userRepository.save(joinRequest.getData(joinRequest))
    }

    fun login(loginRequest: LoginRequest): TokenResponse {
        try {
            val username: String = loginRequest.email
            val password: String = loginRequest.password
            val authenticator = UsernamePasswordAuthenticationToken(username, password)

            authenticationManager.authenticate(authenticator)

            val accessToken: String = jwtTokenProvider.createAccessToken(username)
            val refreshToken: String = jwtTokenProvider.createRefreshToken(username)

            return TokenResponse(accessToken, refreshToken, "Bearer")
        } catch (e: Exception) {
            throw LoginFailedException("invalid email/password supplied")
        }
    }
}