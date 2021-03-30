package com.birth.fit.domain.repository

import com.birth.fit.domain.entity.HelpLike
import com.birth.fit.domain.entity.QnaLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QnaLikeRepository: JpaRepository<QnaLike, Any> {
    fun findByQnaIdAndUserEmail(qnaId: Int, userEmail: String): QnaLike?
}