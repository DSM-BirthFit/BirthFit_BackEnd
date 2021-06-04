package com.birth.fit.domain.help.domain.entity

import com.birth.fit.domain.user.domain.entity.User
import java.io.Serializable

class HelpLikePK(

    private var help: Help,
    private var user: User
): Serializable {
    private val serialVersionUID: Long = 1L

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HelpLikePK

        if (help.id != other.help.id) return false
        if (user.email != other.user.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = user.email.hashCode()
        result = 31 * result + help.id!!
        return result
    }
}