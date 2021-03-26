package com.birth.fit.domain.entity

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass

@Entity
@IdClass(QnaLikePK::class)
class QnaLike(

    @Id
    internal val userEmail: String,

    @Id
    internal val qnaId: Int
): Serializable {
    private val serialVersionUID: Long = 1L
}