package com.birth.fit.service

import com.birth.fit.util.JwtTokenProvider
import com.birth.fit.domain.entity.Email
import com.birth.fit.domain.entity.User
import com.birth.fit.domain.repository.EmailRepository
import com.birth.fit.domain.repository.UserRepository
import com.birth.fit.dto.JoinRequest
import com.birth.fit.dto.LoginRequest
import com.birth.fit.dto.TokenResponse
import com.birth.fit.exception.error.InvalidAuthEmailException
import com.birth.fit.exception.error.LoginFailedException
import com.birth.fit.exception.error.UserNotFoundException
import com.birth.fit.util.CipherUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey
import java.util.*

@Service
class UserService(
    @Autowired val userRepository: UserRepository,
    @Autowired val emailRepository: EmailRepository,
    @Autowired val jwtTokenProvider: JwtTokenProvider,
    @Autowired val cipherUtil: CipherUtil
) {
    private val keyPair: KeyPair = cipherUtil.genRSAKeyPair()
    private val publicKey: PublicKey = keyPair.public
    private val privateKey: PrivateKey = keyPair.private

    fun join(joinRequest: JoinRequest) {
        emailRepository.findById(joinRequest.email)
            .filter(Email::isVerified)
            .orElseThrow {
                InvalidAuthEmailException("This email is not authenticated.")
            }

        joinRequest.password = encodePassword(joinRequest.password)
        userRepository.save(joinRequest.getData(joinRequest))
    }

    fun login(loginRequest: LoginRequest): TokenResponse {
        val username: String = loginRequest.email
        val password: String = loginRequest.password

        val user: User? = userRepository.findByEmail(username)
        user?: throw UserNotFoundException("This email is not subscribed to.")

        if(decodePassword(user.password) != password) {
            throw LoginFailedException("Passwords do not match.")
        }

        val accessToken: String = jwtTokenProvider.createAccessToken(username)
        val refreshToken: String = jwtTokenProvider.createRefreshToken(username)

        return TokenResponse(accessToken, refreshToken, "Bearer")
    }

    private fun encodePassword(password: String): String {
        val bytePublicKey = publicKey.encoded
        val base64PublicKey: String = Base64.getEncoder().encodeToString(bytePublicKey)
        val puKey: PublicKey = cipherUtil.getPublicKeyFromBase64String(base64PublicKey)

        return cipherUtil.encryptRSA(password, puKey)
    }

    private fun decodePassword(encodePassword: String): String {
        val bytePrivateKey = privateKey.encoded
        val base64PrivateKey = Base64.getEncoder().encodeToString(bytePrivateKey)
        val prKey: PrivateKey = cipherUtil.getPrivateKeyFromBase64String(base64PrivateKey)

        return cipherUtil.decryptRSA(encodePassword, prKey)
    }
}