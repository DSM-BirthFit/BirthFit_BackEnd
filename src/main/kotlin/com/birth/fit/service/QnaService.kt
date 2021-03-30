package com.birth.fit.service

import com.birth.fit.domain.entity.*
import com.birth.fit.domain.repository.QnaAnswerRepository
import com.birth.fit.domain.repository.QnaLikeRepository
import com.birth.fit.domain.repository.QnaRepository
import com.birth.fit.domain.repository.UserRepository
import com.birth.fit.dto.*
import com.birth.fit.exception.error.ExpiredTokenException
import com.birth.fit.exception.error.PostNotFoundException
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

    fun getList(bearerToken: String?, pageable: Pageable): QnaPageResponse? {
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
                        like = qna.likeCount
                    )
                )
            }
        }
        return QnaPageResponse(
            totalElement = qnaList.totalElements.toInt(),
            totalPage = qnaList.totalPages,
            listResponse = list
        )
    }

    fun getContent(bearerToken: String?, qnaId: Int): QnaContentResponse {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val qna: Qna? = qnaRepository.findById(qnaId)
        qna?: throw PostNotFoundException("Post not Found")

        val author: User? = userRepository.findByEmail(qna.userEmail)

        val list: MutableList<QnaAnswerResponse> = ArrayList()
        val answer: MutableList<QnaAnswer>? = qnaAnswerRepository.findAllByQnaId(qnaId)
        answer?.run {
            this.forEach {
                val writer: User = userRepository.findByEmail(it.userEmail)!!
                list.add(
                    QnaAnswerResponse(
                        qnaId = it.qnaId,
                        userId = writer.userId,
                        content = it.content,
                        isMine = writer.email == user.email
                    )
                )
            }
        }

        qnaRepository.save(qna.view())
        return QnaContentResponse(
            title = qna.title,
            content = qna.content,
            userId = author!!.userId,
            createdAt = qna.createdAt,
            view = qna.view,
            like = qna.likeCount,
            isMine = user.email == author.email,
            isLike = qnaLikeRepository.findByQnaIdAndUserEmail(qnaId, user.email) != null,
            answer = list
        )
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

    fun updateQna(bearerToken: String?, qnaId: Int, postRequest: PostRequest) {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val qna: Qna? = qnaRepository.findById(qnaId);
        qna?: throw PostNotFoundException("Post not Found.")

        qnaRepository.save(qna.updateContent(postRequest))
    }

    fun deleteQna(bearerToken: String?, qnaId: Int) {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val qna: Qna? = qnaRepository.findById(qnaId)
        qna?: throw PostNotFoundException("Post not Found.")

        qnaRepository.delete(qna)
    }
}