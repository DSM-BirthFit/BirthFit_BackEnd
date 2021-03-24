package com.birth.fit.domain.entity

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass

@Entity
@IdClass(HelpLikePK::class)
class HelpLike(

    @Id
    private val userEmail: String,

    @Id
    private val helpId: Int
): Serializable {
    private val serialVersionUID: Long = 1L
}