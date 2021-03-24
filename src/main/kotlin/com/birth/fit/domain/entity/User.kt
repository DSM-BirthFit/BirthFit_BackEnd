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
    private val help: List<Help>? = null
)