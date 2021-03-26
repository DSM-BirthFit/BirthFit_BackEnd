package com.birth.fit.domain.entity

import javax.persistence.*

@Entity
@Table(name = "user")
class User(

    @Id
    @Column(name = "email")
    internal val email: String,

    @Column(name = "user_id")
    internal var userId: String,

    @Column(name = "password")
    internal var password: String,

    @OneToMany(mappedBy = "userEmail")
    private val help: MutableList<Help>? = null,

    @OneToMany(mappedBy = "userEmail")
    private val helpLike: MutableList<HelpLike>? = null,

    @OneToMany(mappedBy = "userEmail")
    private val comment: MutableList<HelpComment>? = null
)