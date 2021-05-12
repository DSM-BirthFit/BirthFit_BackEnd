package com.birth.fit.domain.user.service

import com.birth.fit.common.exception.error.*
import com.birth.fit.common.s3.S3Service
import com.birth.fit.common.util.AES256Util
import com.birth.fit.common.util.JwtTokenProvider
import com.birth.fit.domain.email.domain.entity.Email
import com.birth.fit.domain.email.domain.repository.EmailRepository
import com.birth.fit.domain.user.domain.entity.User
import com.birth.fit.domain.user.domain.repository.UserRepository
import com.birth.fit.domain.user.dto.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    @Autowired private val s3Service: S3Service,
    @Autowired private val aes256Util: AES256Util,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val emailRepository: EmailRepository,
    @Autowired private val jwtTokenProvider: JwtTokenProvider
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
            email = user.email,
            userId = user.userId,
            image = user.image
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

        profileRequest.userId?.let { user.userId = profileRequest.userId }
        if(profileRequest.password != "" && aes256Util.aesDecode(user.password) == profileRequest.password) {
            throw PasswordSameException("The password are same before.")
        }

        if(profileRequest.password != "") {
            user.password = aes256Util.aesEncode(profileRequest.password!!)
        }

        profileRequest.image?.let { its ->
            val imageName: String = UUID.randomUUID().toString()

            user.image?.let { it ->
                s3Service.delete(it)
            }

            s3Service.upload(its, imageName);
            user.image = imageName
        }

        userRepository.save(user)
    }

    fun deleteUser(passwordRequest: PasswordRequest) {
        val token: String? = jwtTokenProvider.getToken("Authorization")
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        if(aes256Util.aesDecode(user.password) != passwordRequest.password) {
            throw PasswordNotSameException("비밀번호가 일치하지 않습니다.")
        }
        
        userRepository.delete(user)
    }

    private fun tokenResponse(username: String): TokenResponse {
        val accessToken: String = jwtTokenProvider.createAccessToken(username)
        val refreshToken: String = jwtTokenProvider.createRefreshToken(username)


        return TokenResponse(accessToken, refreshToken, "Bearer")
    }
}