package com.birth.fit.dto

import com.birth.fit.domain.entity.User

class JoinRequest(
    val email: String,
    var userId: String,
    var password: String
) {
    fun getData(joinRequest: JoinRequest): User {
        return User(
            joinRequest.email,
            joinRequest.userId,
            joinRequest.password
        )
    }
}