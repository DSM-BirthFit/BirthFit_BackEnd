package com.birth.fit.domain.qna.domain.entity

import com.birth.fit.domain.user.domain.entity.User
import javax.persistence.*

@Entity
class QnaAnswer(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    internal val answerId: Int? = null,
    internal var answer: String,

    @ManyToOne
    @JoinColumn(name = "qna_id", nullable = false)
    internal val qna: Qna,

    @ManyToOne
    @JoinColumn(name = "userEmail", nullable = false)
    internal val user: User
) {

    fun updateAnswer(answer: String): QnaAnswer {
        this.answer = answer
        return this
    }
}