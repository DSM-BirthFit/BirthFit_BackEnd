package com.birth.fit.service

import com.birth.fit.domain.entity.Help
import com.birth.fit.domain.entity.HelpComment
import com.birth.fit.domain.entity.HelpLike
import com.birth.fit.domain.entity.User
import com.birth.fit.domain.repository.HelpAnswerRepository
import com.birth.fit.domain.repository.HelpLikeRepository
import com.birth.fit.domain.repository.HelpRepository
import com.birth.fit.domain.repository.UserRepository
import com.birth.fit.dto.HelpCommentResponse
import com.birth.fit.dto.HelpContentResponse
import com.birth.fit.dto.HelpListResponse
import com.birth.fit.dto.PostRequest
import com.birth.fit.exception.error.ExpiredTokenException
import com.birth.fit.exception.error.PostNotFoundException
import com.birth.fit.exception.error.UserNotFoundException
import com.birth.fit.util.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.collections.ArrayList

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

    fun getContent(bearerToken: String?, helpId: Int): HelpContentResponse? {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val help: Help? = helpRepository.findById(helpId)
        help?: throw PostNotFoundException("Post not Found")

        val author: User? = userRepository.findByEmail(help.userEmail)

        val list: MutableList<HelpCommentResponse> = ArrayList()
        val answers: MutableList<HelpComment>? = helpAnswerRepository.findAllByHelpId(helpId)
        answers?.run {
            this.forEach {
                val writer: User = userRepository.findByEmail(it.userEmail)!!
                list.add(
                    HelpCommentResponse(
                        userId = writer.userId,
                        content = it.content,
                        isMine = writer.email == user.email
                    )
                )
            }
        }

        helpRepository.save(help.view())
        return HelpContentResponse(
            title = help.title,
            content = help.content,
            userId = user.userId,
            createdAt = help.createdAt,
            view = help.view,
            like = helpLikeRepository.countByHelpId(helpId),
            isMine = user.email == author!!.email,
            isLike = helpLikeRepository.findByHelpIdAndUserEmail(helpId, user.email) != null,
            answer = list
        )
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

    fun updateHelp(bearerToken: String?, helpId: Int, postRequest: PostRequest) {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val help: Help? = helpRepository.findById(helpId)
        help?: throw PostNotFoundException("Post not Found")

        helpRepository.save(help.updateContent(postRequest))
    }

    fun like(bearerToken: String?, helpId: Int) {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val help: Help? = helpRepository.findById(helpId)
        help?: throw PostNotFoundException("Post not Found")

        val like: HelpLike? = helpLikeRepository.findByHelpIdAndUserEmail(helpId, user.email)
        if(like == null) {
            helpLikeRepository.save(
                HelpLike(
                    userEmail = user.email,
                    helpId = helpId
                )
            )
        } else {
            helpLikeRepository.delete(like)
        }
    }

    fun deleteHelp(bearerToken: String?, helpId: Int) {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val help: Help? = helpRepository.findById(helpId)
        help?: throw PostNotFoundException("Post not Found")

        helpRepository.delete(help)
    }
}