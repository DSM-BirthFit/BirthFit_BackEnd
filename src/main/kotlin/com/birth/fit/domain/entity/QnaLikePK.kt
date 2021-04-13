package com.birth.fit.domain.entity

import java.io.Serializable

class QnaLikePK(

    private var userEmail: String = "",
    private var qnaId: Int = 0
): Serializable {
    private val serialVersionUID: Long = 1L

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QnaLikePK

        if (userEmail != other.userEmail) return false
        if (qnaId != other.qnaId) return false
        if (serialVersionUID != other.serialVersionUID) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userEmail.hashCode()
        result = 31 * result + qnaId
        result = 31 * result + serialVersionUID.hashCode()
        return result
    }

}