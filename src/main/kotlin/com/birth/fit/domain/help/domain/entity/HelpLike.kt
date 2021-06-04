package com.birth.fit.domain.help.domain.entity

import com.birth.fit.domain.user.domain.entity.User
import java.io.Serializable
import javax.persistence.*

@Entity
@IdClass(HelpLikePK::class)
class HelpLike(

    @Id
    @ManyToOne
    @JoinColumn(name = "help_id", nullable = false)
    private val help: Help,

    @Id
    @ManyToOne
    @JoinColumn(name = "userEmail", nullable = false)
    private val user: User,
): Serializable {
    private val serialVersionUID: Long = 1L
}