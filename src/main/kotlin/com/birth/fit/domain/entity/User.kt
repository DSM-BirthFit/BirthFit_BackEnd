package com.birth.fit.domain.entity

import javax.persistence.*

@Entity
@Table(name = "user")
class User(

    @Id
    @Column(name = "email")
    var email: String,

    @Column(name = "user_id")
    var userId: String,

    @Column(name = "password")
    var password: String
    ) {
    fun update(userId: String, password: String) {
        this.userId = userId
        this.password = password
    }

}