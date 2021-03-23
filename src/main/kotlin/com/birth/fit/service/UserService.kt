package com.birth.fit.service

import com.birth.fit.domain.entity.Email
import com.birth.fit.domain.entity.User
import com.birth.fit.domain.repository.EmailRepository
import com.birth.fit.domain.repository.UserRepository
import com.birth.fit.dto.*
import com.birth.fit.exception.error.ExpiredTokenException
import com.birth.fit.exception.error.InvalidAuthEmailException
import com.birth.fit.exception.error.LoginFailedException
import com.birth.fit.exception.error.UserNotFoundException
import com.birth.fit.util.AES256Util
import com.birth.fit.util.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired val userRepository: UserRepository,
    @Autowired val emailRepository: EmailRepository,
    @Autowired val jwtTokenProvider: JwtTokenProvider,
    @Autowired val aes256Util: AES256Util
) {

    fun getProfile(bearerToken: String): ProfileResponse {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        return ProfileResponse(
            user.email,
            user.userId
        )
    }

    fun join(joinRequest: JoinRequest) {
        emailRepository.findById(joinRequest.email)
            .filter(Email::isVerified)
            .orElseThrow {
                InvalidAuthEmailException("This email is not authenticated.")
            }

        joinRequest.password = aes256Util.aesEncode(joinRequest.password)
        userRepository.save(joinRequest.getData(joinRequest))
    }

    fun login(loginRequest: LoginRequest): TokenResponse {
        val username: String = loginRequest.email
        val password: String = loginRequest.password

        val user: User? = userRepository.findByEmail(username)
        user?: throw UserNotFoundException("This email is not subscribed to.")

        if(aes256Util.aesDecode(user.password) != password) {
            throw LoginFailedException("Passwords do not match.")
        }

        val accessToken: String = jwtTokenProvider.createAccessToken(username)
        val refreshToken: String = jwtTokenProvider.createRefreshToken(username)

        return TokenResponse(accessToken, refreshToken, "Bearer")
    }

    fun findPassword(passwordRequest: ChangePasswordRequest) {
        val email: String = passwordRequest.email
        emailRepository.findById(email)
            .filter(Email::isVerified)
            .orElseThrow {
                InvalidAuthEmailException("This email is not authenticated.")
            }

        val user: User? = userRepository.findByEmail(email)
        user?: throw UserNotFoundException("This email is not subscribed to.")

        user.password = aes256Util.aesEncode(passwordRequest.password)
        userRepository.save(user)
    }

    fun changeProfile(bearerToken: String, profileRequest: ChangeProfileRequest) {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        if(user.userId != profileRequest.userId) user.userId = profileRequest.userId
        profileRequest.password?.let { user.password = profileRequest.password }

        userRepository.save(user)
    }
}