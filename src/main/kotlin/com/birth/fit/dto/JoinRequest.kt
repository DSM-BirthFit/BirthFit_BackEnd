package com.birth.fit.dto

import com.birth.fit.domain.entity.User

class JoinRequest(
    val email: String,
    val id: String,
    val password: String
) {
    fun getData(): User {
        return User(
            email = email,
            id = id,
            password = password
        )
    }
}