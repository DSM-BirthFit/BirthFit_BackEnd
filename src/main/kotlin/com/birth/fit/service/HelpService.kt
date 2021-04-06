package com.birth.fit.service

import com.birth.fit.domain.entity.Help
import com.birth.fit.domain.entity.HelpComment
import com.birth.fit.domain.entity.HelpLike
import com.birth.fit.domain.entity.User
import com.birth.fit.domain.repository.*
import com.birth.fit.dto.*
import com.birth.fit.exception.error.ContentNotFoundException
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
    @Autowired val helpCommentRepository: HelpCommentRepository,
    @Autowired val jwtTokenProvider: JwtTokenProvider
) {

    fun getList(bearerToken: String?, pageable: Pageable): HelpPageResponse? {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val helps: Page<Help> = helpRepository.findAll(pageable)
        val list: MutableList<HelpListResponse> = ArrayList()

        helps.let {
            for(help in it) {
                list.add(
                    HelpListResponse(
                        helpId = help.id!!,
                        title = help.title,
                        comment = helpCommentRepository.countByHelpId(help.id!!),
                        like = help.likeCount
                    )
                )
            }
        }
        return HelpPageResponse(
            totalElement = helps.totalElements.toInt(),
            totalPage = helps.totalPages,
            listResponse = list
        )
    }

    fun getContent(bearerToken: String?, helpId: Int): HelpContentResponse? {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val help: Help? = helpRepository.findById(helpId)
        help?: throw PostNotFoundException("Post not Found")

        val author: User = userRepository.findByEmail(help.userEmail)!!

        val list: MutableList<HelpCommentResponse> = ArrayList()
        val comment: MutableList<HelpComment>? = helpCommentRepository.findAllByHelpId(helpId)
        comment?.run {
            this.forEach {
                val writer: User = userRepository.findByEmail(it.userEmail)!!
                list.add(
                    HelpCommentResponse(
                        commentId = it.commentId!!,
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
            userId = author.userId,
            createdAt = help.createdAt.toString().substring(0, 10),
            view = help.view,
            like = help.likeCount,
            isMine = user.email == author!!.email,
            isLike = helpLikeRepository.findByUserEmailAndHelpId(user.email, helpId)?.isPresent == true,
            comment = list
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

    fun writeComment(bearerToken: String?, helpId: Int, contentRequest: ContentRequest) {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val help: Help? = helpRepository.findById(helpId)
        help?: throw PostNotFoundException("Post not Found")

        helpCommentRepository.save(
            HelpComment(
                userEmail = user.email,
                helpId = helpId,
                content = contentRequest.content
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
            helpRepository.save(help.like())
        } else {
            helpLikeRepository.delete(like)
            helpRepository.save(help.unLike())
        }
    }

    fun updateComment(bearerToken: String?, commentId: Int, contentRequest: ContentRequest) {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val comment: HelpComment? = helpCommentRepository.findByCommentId(commentId)
        comment?: throw ContentNotFoundException("Comments do not exist.")

        helpCommentRepository.save(comment.updateComment(contentRequest.content))
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

    fun deleteComment(bearerToken: String?, commentId: Int) {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val comment: HelpComment? = helpCommentRepository.findByCommentId(commentId)
        comment?: throw ContentNotFoundException("Comments do not exist.")

        helpCommentRepository.delete(comment)
    }
}