package com.birth.fit.domain.qna.domain.entity

import com.birth.fit.domain.user.domain.entity.User
import java.io.Serializable
import javax.persistence.*

@Entity
@IdClass(QnaLikePK::class)
class QnaLike(

    @Id
    @ManyToOne
    @JoinColumn(name = "qna_id", nullable = false)
    private val qna: Qna,

    @Id
    @ManyToOne
    @JoinColumn(name = "userEmail", nullable = false)
    private val user: User,
): Serializable {
    private val serialVersionUID: Long = 1L
}