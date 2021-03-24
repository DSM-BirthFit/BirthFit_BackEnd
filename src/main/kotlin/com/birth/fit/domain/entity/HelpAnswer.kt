package com.birth.fit.domain.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class HelpAnswer(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val answerId: Int,
    private val helpId: Int,
    private val userEmail: String,
    private val content: String
)