package com.birth.fit.service

import com.birth.fit.domain.entity.Help
import com.birth.fit.domain.entity.User
import com.birth.fit.domain.repository.HelpRepository
import com.birth.fit.domain.repository.UserRepository
import com.birth.fit.dto.PostRequest
import com.birth.fit.exception.error.ExpiredTokenException
import com.birth.fit.exception.error.UserNotFoundException
import com.birth.fit.util.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HelpService(
    @Autowired val userRepository: UserRepository,
    @Autowired val helpRepository: HelpRepository,
    @Autowired val jwtTokenProvider: JwtTokenProvider
) {

    fun write(bearerToken: String?, postRequest: PostRequest){
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        helpRepository.save(
            Help(
                userEmail = user.email,
                title = postRequest.title,
                content = postRequest.content
            )
        )
    }
}