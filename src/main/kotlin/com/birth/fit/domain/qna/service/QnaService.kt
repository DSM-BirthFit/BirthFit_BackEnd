package com.birth.fit.domain.qna.service

import com.birth.fit.common.exception.error.ContentNotFoundException
import com.birth.fit.common.exception.error.ExpiredTokenException
import com.birth.fit.common.exception.error.PostNotFoundException
import com.birth.fit.common.exception.error.UserNotFoundException
import com.birth.fit.common.util.JwtTokenProvider
import com.birth.fit.domain.qna.domain.entity.Qna
import com.birth.fit.domain.qna.domain.entity.QnaAnswer
import com.birth.fit.domain.qna.domain.entity.QnaLike
import com.birth.fit.domain.qna.domain.repository.QnaAnswerRepository
import com.birth.fit.domain.qna.domain.repository.QnaLikeRepository
import com.birth.fit.domain.qna.domain.repository.QnaRepository
import com.birth.fit.domain.qna.dto.*
import com.birth.fit.domain.user.domain.entity.User
import com.birth.fit.domain.user.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class QnaService(
    @Autowired private val qnaRepository: QnaRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val qnaLikeRepository: QnaLikeRepository,
    @Autowired private val qnaAnswerRepository: QnaAnswerRepository,
    @Autowired private val jwtTokenProvider: JwtTokenProvider
) {

    fun getList(pageable: Pageable): QnaPageResponse? {
        val token: String? = jwtTokenProvider.getToken("Authorization")
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val qnaList: Page<Qna> = qnaRepository.findAllByOrderByIdDesc(pageable)
        val list: MutableList<QnaListResponse> = ArrayList()

        qnaList.forEach {
            list.add(
                QnaListResponse(
                    qnaId = it.id!!,
                    title = it.title,
                    answer = qnaAnswerRepository.countByQnaId(it.id!!),
                    like = it.likeCount
                )
            )
        }

        return QnaPageResponse(
            totalElement = qnaList.totalElements.toInt(),
            totalPage = qnaList.totalPages,
            listResponse = list
        )
    }

    fun getContent(qnaId: Int): QnaContentResponse {
        val token: String? = jwtTokenProvider.getToken("Authorization")
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val qna: Qna? = qnaRepository.findById(qnaId)
        qna?: throw PostNotFoundException("Post not Found")

        val author: User = userRepository.findByEmail(qna.userEmail)!!

        qnaRepository.save(qna.view())
        return QnaContentResponse(
            title = qna.title,
            content = qna.content,
            userId = author.userId,
            userImage = author.image,
            createdAt = qna.createdAt.toString(),
            view = qna.view,
            likeCount = qna.likeCount,
            isMine = user.email == author.email,
            isLike = qnaLikeRepository.findByUserEmailAndQnaId(user.email, qnaId)?.isPresent == true,
            answer = qnaAnswerRepository.findAllByQnaId(qnaId)?.map {
                val writer: User = userRepository.findByEmail(it.userEmail)!!
                QnaAnswerResponse(
                    answerId = it.answerId!!,
                    userId = writer.userId,
                    userImage = writer.image,
                    answer = it.answer,
                    isMine = writer.email == user.email
                )
            }
        )
    }

    fun write(qnaPostRequest: QnaPostRequest) {
        val token: String? = jwtTokenProvider.getToken("Authorization")
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        qnaRepository.save(
            Qna(
                userEmail = user.email,
                title = qnaPostRequest.title,
                content = qnaPostRequest.content,
                createdAt = LocalDate.now()
            )
        )
    }

    fun writeAnswer(qnaId: Int, qnaAnswerRequest: QnaAnswerRequest) {
        val token: String? = jwtTokenProvider.getToken("Authorization")
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val qna: Qna? = qnaRepository.findById(qnaId)
        qna?: throw PostNotFoundException("Post not found.")

        qnaAnswerRepository.save(
            QnaAnswer(
                qnaId = qnaId,
                userEmail = user.email,
                answer = qnaAnswerRequest.answer
            )
        )
    }

    fun updateQna(qnaId: Int, qnaPostRequest: QnaPostRequest) {
        val token: String? = jwtTokenProvider.getToken("Authorization")
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val qna: Qna? = qnaRepository.findById(qnaId);
        qna?: throw PostNotFoundException("Post not found.")

        qnaRepository.save(qna.updateContent(qnaPostRequest))
    }

    fun like(qnaId: Int) {
        val token: String? = jwtTokenProvider.getToken("Authorization")
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val qna: Qna? = qnaRepository.findById(qnaId)
        qna?: throw PostNotFoundException("Post not found")

        val like: QnaLike? =qnaLikeRepository.findByQnaIdAndUserEmail(qnaId, user.email)
        if(like == null) {
            qnaLikeRepository.save(
                QnaLike(
                    userEmail = user.email,
                    qnaId = qnaId
                )
            )
            qnaRepository.save(qna.like())
        } else {
            qnaLikeRepository.delete(like)
            qnaRepository.save(qna.unLike())
        }
    }

    fun updateAnswer(answerId: Int, qnaAnswerRequest: QnaAnswerRequest) {
        val token: String? = jwtTokenProvider.getToken("Authorization")
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val answer: QnaAnswer? = qnaAnswerRepository.findByAnswerId(answerId)
        answer?: throw ContentNotFoundException("Comments do not exist.")

        qnaAnswerRepository.save(answer.updateAnswer(qnaAnswerRequest.answer))
    }

    fun deleteQna(qnaId: Int) {
        val token: String? = jwtTokenProvider.getToken("Authorization")
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val qna: Qna? = qnaRepository.findById(qnaId)
        qna?: throw PostNotFoundException("Post not found.")

        qnaRepository.delete(qna)
    }

    fun deleteAnswer(answerId: Int) {
        val token: String? = jwtTokenProvider.getToken("Authorization")
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val answer: QnaAnswer? = qnaAnswerRepository.findByAnswerId(answerId)
        answer?: throw ContentNotFoundException("Answer do not exist.")

        qnaAnswerRepository.delete(answer)
    }
}