package com.birth.fit.service

import com.birth.fit.domain.entity.Help
import com.birth.fit.domain.entity.User
import com.birth.fit.domain.repository.HelpAnswerRepository
import com.birth.fit.domain.repository.HelpLikeRepository
import com.birth.fit.domain.repository.HelpRepository
import com.birth.fit.domain.repository.UserRepository
import com.birth.fit.dto.HelpListResponse
import com.birth.fit.dto.PostRequest
import com.birth.fit.exception.error.ExpiredTokenException
import com.birth.fit.exception.error.UserNotFoundException
import com.birth.fit.util.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class HelpService(
    @Autowired val userRepository: UserRepository,
    @Autowired val helpRepository: HelpRepository,
    @Autowired val helpLikeRepository: HelpLikeRepository,
    @Autowired val helpAnswerRepository: HelpAnswerRepository,
    @Autowired val jwtTokenProvider: JwtTokenProvider
) {

    fun getList(bearerToken: String?, pageable: Pageable): MutableList<HelpListResponse>? {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val helps: Page<Help> = helpRepository.findAll(pageable)
        val list: MutableList<HelpListResponse> = ArrayList()

        helps.let {
            for(help in helps) {
                val user: User? = userRepository.findByEmail(help.userEmail)
                user?: throw UserNotFoundException("User not found.")

                list.add(
                    HelpListResponse(
                        helpId = help.id!!,
                        title = help.title,
                        userEmail = user.email,
                        answer = helpAnswerRepository.countByHelpId(help.id!!),
                        like = helpLikeRepository.countByHelpId(help.id!!)
                    )
                )
            }
        }
        return list
    }

    fun write(bearerToken: String?, postRequest: PostRequest){
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        helpRepository.save(
            Help(
                userEmail = user.email,
                title = postRequest.title,
                content = postRequest.content,
                createdAt = LocalDateTime.now()
            )
        )
    }
}