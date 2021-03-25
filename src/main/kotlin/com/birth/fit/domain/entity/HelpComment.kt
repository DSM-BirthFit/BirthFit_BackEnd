package com.birth.fit.domain.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class HelpComment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    internal val commentId: Int? = null,
    internal val helpId: Int,
    internal val userEmail: String,
    internal val content: String
)