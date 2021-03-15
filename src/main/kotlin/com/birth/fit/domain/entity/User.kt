package com.birth.fit.domain.entity

import javax.persistence.*

@Entity
@Table(name = "user")
class User(
    @Id
    var email: String,

    @Column(name = "id")
    var id: String,

    @Column(name = "password")
    var password: String
    ) {
    fun update(id: String, password: String) {
        this.id = id
        this.password = password
    }

}