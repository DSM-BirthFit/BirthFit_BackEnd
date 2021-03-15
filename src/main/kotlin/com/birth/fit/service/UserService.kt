package com.birth.fit.service

import com.birth.fit.domain.repository.UserRepository
import com.birth.fit.dto.JoinRequest
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository
) {
    fun join(joinRequest: JoinRequest) {
        userRepository.save(joinRequest.getData())
    }
}