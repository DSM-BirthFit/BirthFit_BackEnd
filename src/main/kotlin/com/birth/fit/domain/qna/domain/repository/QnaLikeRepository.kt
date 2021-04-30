package com.birth.fit.domain.qna.domain.repository

import com.birth.fit.domain.qna.domain.entity.QnaLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface QnaLikeRepository: JpaRepository<QnaLike, Any> {
    fun findByQnaIdAndUserEmail(qnaId: Int, userEmail: String): QnaLike?
    fun findByUserEmailAndQnaId(userEmail: String, qnaId: Int): Optional<QnaLike>?
}