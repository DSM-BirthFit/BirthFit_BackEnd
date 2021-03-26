package com.birth.fit.service

import com.birth.fit.domain.entity.Qna
import com.birth.fit.domain.entity.User
import com.birth.fit.domain.repository.QnaRepository
import com.birth.fit.domain.repository.UserRepository
import com.birth.fit.dto.PostRequest
import com.birth.fit.exception.error.ExpiredTokenException
import com.birth.fit.exception.error.UserNotFoundException
import com.birth.fit.util.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class QnaService(
    @Autowired private val qnaRepository: QnaRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val jwtTokenProvider: JwtTokenProvider
) {

    fun write(bearerToken: String?, postRequest: PostRequest) {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        qnaRepository.save(
            Qna(
                userEmail = user.email,
                title = postRequest.title,
                content = postRequest.content,
                createdAt = LocalDateTime.now()
            )
        )
    }
}