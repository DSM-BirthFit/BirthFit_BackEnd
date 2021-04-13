package com.birth.fit.service

import com.birth.fit.domain.entity.Email
import com.birth.fit.domain.entity.User
import com.birth.fit.domain.repository.EmailRepository
import com.birth.fit.domain.repository.UserRepository
import com.birth.fit.dto.*
import com.birth.fit.exception.error.*
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

        userRepository.existsByUserId(joinRequest.userId)?.run {
            throw UserAlreadyExistException("UserId Already Exists.")
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

        return tokenResponse(user.email)
    }

    fun refreshToken(refreshToken: String?): TokenResponse {
        val token: String? = jwtTokenProvider.resolveToken(refreshToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        if(jwtTokenProvider.getType(token) != "refresh_token") throw InvalidTokenException("Token type error.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        return tokenResponse(user.email)
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

        if(user.password == passwordRequest.password) {
            throw PasswordSameException("The password are same before.")
        }

        user.password = aes256Util.aesEncode(passwordRequest.password)
        userRepository.save(user)
    }

    fun changeProfile(bearerToken: String, profileRequest: ChangeProfileRequest) {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        if(user.userId != profileRequest.userId) user.userId = profileRequest.userId
        if(profileRequest.password != null && user.password == profileRequest.password) {
            throw PasswordSameException("The password are same before.")
        }

        profileRequest.password?.let { user.password = aes256Util.aesEncode(profileRequest.password) }

        userRepository.save(user)
    }

    private fun tokenResponse(username: String): TokenResponse {
        val accessToken: String = jwtTokenProvider.createAccessToken(username)
        val refreshToken: String = jwtTokenProvider.createRefreshToken(username)


        return TokenResponse(accessToken, refreshToken, "Bearer")
    }
}