package com.birth.fit.service

import com.birth.fit.domain.entity.Qna
import com.birth.fit.domain.entity.User
import com.birth.fit.domain.repository.QnaAnswerRepository
import com.birth.fit.domain.repository.QnaLikeRepository
import com.birth.fit.domain.repository.QnaRepository
import com.birth.fit.domain.repository.UserRepository
import com.birth.fit.dto.HelpListResponse
import com.birth.fit.dto.PostRequest
import com.birth.fit.dto.QnaListResponse
import com.birth.fit.exception.error.ExpiredTokenException
import com.birth.fit.exception.error.UserNotFoundException
import com.birth.fit.util.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class QnaService(
    @Autowired private val qnaRepository: QnaRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val qnaLikeRepository: QnaLikeRepository,
    @Autowired private val qnaAnswerRepository: QnaAnswerRepository,
    @Autowired private val jwtTokenProvider: JwtTokenProvider
) {

    fun getList(bearerToken: String?, pageable: Pageable): MutableList<QnaListResponse> {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val qnaList: Page<Qna> = qnaRepository.findAll(pageable)
        val list: MutableList<QnaListResponse> = ArrayList()

        qnaList.let {
            for(qna in it) {
                list.add(
                    QnaListResponse(
                        qnaId = qna.id!!,
                        title = qna.title,
                        answer = qnaAnswerRepository.countByQnaId(qna.id!!),
                        like = qnaLikeRepository.countByQnaId(qna.id!!)
                    )
                )
            }
        }
        return list
    }

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