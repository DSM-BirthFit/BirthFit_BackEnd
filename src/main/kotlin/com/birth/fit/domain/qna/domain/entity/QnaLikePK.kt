package com.birth.fit.domain.qna.domain.entity

import com.birth.fit.domain.user.domain.entity.User
import java.io.Serializable

class QnaLikePK(

    private var qna: Qna,
    private var user: User
): Serializable {
    private val serialVersionUID: Long = 1L

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QnaLikePK

        if (qna.id != other.qna.id) return false
        if (user.email != other.user.email) return false
        if (serialVersionUID != other.serialVersionUID) return false

        return true
    }

    override fun hashCode(): Int {
        var result = user.email.hashCode()
        result = 31 * result + qna.id!!
        result = 31 * result + serialVersionUID.hashCode()
        return result
    }

}