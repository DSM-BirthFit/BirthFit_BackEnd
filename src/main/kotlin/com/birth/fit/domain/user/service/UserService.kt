package com.birth.fit.domain.user.service

import com.birth.fit.domain.email.domain.entity.Email
import com.birth.fit.domain.user.domain.entity.User
import com.birth.fit.domain.email.domain.repository.EmailRepository
import com.birth.fit.domain.user.domain.repository.UserRepository
import com.birth.fit.common.exception.error.*
import com.birth.fit.common.util.AES256Util
import com.birth.fit.common.util.JwtTokenProvider
import com.birth.fit.domain.user.dto.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.util.*

@Service
class UserService(
    @Value("\${image.upload.dir}")
    private val imageDirPath: String,

    @Autowired private val userRepository: UserRepository,
    @Autowired private val emailRepository: EmailRepository,
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
    @Autowired private val aes256Util: AES256Util
) {

    fun checkUserId(userId: String): Boolean {
        return userRepository.existsByUserId(userId)
    }

    fun getProfile(): ProfileResponse {
        val token: String? = jwtTokenProvider.getToken("Authorization")
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

        return tokenResponse(user.email)
    }

    fun refreshToken(): TokenResponse {
        val token: String? = jwtTokenProvider.getToken("X-Refresh-Token")
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

    fun changeProfile(profileRequest: ChangeProfileRequest) {
        val token: String? = jwtTokenProvider.getToken("Authorization")
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        if(user.userId != profileRequest.userId) user.userId = profileRequest.userId
        if(profileRequest.password != null && user.password == profileRequest.password) {
            throw PasswordSameException("The password are same before.")
        }

        profileRequest.password?.let { user.password = aes256Util.aesEncode(profileRequest.password) }

        profileRequest.image?.run {
            val imageName: String = UUID.randomUUID().toString()

            user.image?.let {
                File(imageDirPath, it).delete()
            }

            user.image = imageName
        }

        userRepository.save(user)
    }

    private fun tokenResponse(username: String): TokenResponse {
        val accessToken: String = jwtTokenProvider.createAccessToken(username)
        val refreshToken: String = jwtTokenProvider.createRefreshToken(username)


        return TokenResponse(accessToken, refreshToken, "Bearer")
    }
}