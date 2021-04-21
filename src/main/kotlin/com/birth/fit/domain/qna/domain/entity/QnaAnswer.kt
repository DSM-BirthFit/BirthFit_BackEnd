package com.birth.fit.domain.qna.domain.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class QnaAnswer(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    internal val answerId: Int? = null,
    internal val qnaId: Int,
    internal val userEmail: String,
    internal var answer: String
) {

    fun updateAnswer(answer: String): QnaAnswer {
        this.answer = answer
        return this
    }
}