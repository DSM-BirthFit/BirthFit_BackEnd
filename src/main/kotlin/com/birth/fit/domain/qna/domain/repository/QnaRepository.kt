package com.birth.fit.domain.qna.domain.repository

import com.birth.fit.domain.qna.domain.entity.Qna
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QnaRepository: JpaRepository<Qna, String> {
    fun findById(id: Int): Qna?
}