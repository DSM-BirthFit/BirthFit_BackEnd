package com.birth.fit.domain.entity

import java.io.Serializable

class HelpLikePK(

    private var userEmail: String,
    private var helpId: Int
): Serializable {
    private val serialVersionUID: Long = 1L

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HelpLikePK

        if (userEmail != other.userEmail) return false
        if (helpId != other.helpId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userEmail.hashCode()
        result = 31 * result + helpId
        return result
    }
}