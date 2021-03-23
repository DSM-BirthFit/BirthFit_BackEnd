package com.birth.fit.domain.entity

import javax.persistence.*

@Entity
@Table(name = "help")
class Help (

    @Id
    @Column(name = "help_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(name = "user_email")
    val userEmail: String,

    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String
)