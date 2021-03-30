package com.birth.fit.domain.repository

import com.birth.fit.domain.entity.QnaAnswer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QnaAnswerRepository: JpaRepository<QnaAnswer, Int> {
    fun countByQnaId(qnaId: Int): Int?
    fun findAllByQnaId(qnaId: Int): MutableList<QnaAnswer>?
    fun findByAnswerId(answerId: Int): QnaAnswer?
}