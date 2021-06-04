package com.birth.fit.domain.help.domain.entity

import com.birth.fit.domain.user.domain.entity.User
import javax.persistence.*

@Entity
class HelpComment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    internal val commentId: Int? = null,
    internal var comment: String,

    @ManyToOne
    @JoinColumn(name = "help_id", nullable = false)
    internal val help: Help,

    @ManyToOne
    @JoinColumn(name = "userEmail", nullable = false)
    internal val user: User
) {
    fun updateComment(comment: String): HelpComment {
        this.comment = comment
        return this
    }
}