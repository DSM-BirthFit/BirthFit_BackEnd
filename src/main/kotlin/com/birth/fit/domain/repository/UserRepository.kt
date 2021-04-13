package com.birth.fit.domain.repository

import com.birth.fit.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, String> {
    fun findByEmail(email: String): User?

    fun existsByUserId(userId: String): Boolean?
}