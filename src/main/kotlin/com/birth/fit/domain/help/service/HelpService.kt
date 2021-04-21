package com.birth.fit.domain.help.service

import com.birth.fit.domain.help.domain.entity.Help
import com.birth.fit.domain.help.domain.entity.HelpComment
import com.birth.fit.domain.help.domain.entity.HelpLike
import com.birth.fit.domain.user.domain.entity.User
import com.birth.fit.domain.help.domain.repository.HelpCommentRepository
import com.birth.fit.domain.help.domain.repository.HelpLikeRepository
import com.birth.fit.domain.help.domain.repository.HelpRepository
import com.birth.fit.domain.user.domain.repository.UserRepository
import com.birth.fit.common.exception.error.ContentNotFoundException
import com.birth.fit.common.exception.error.ExpiredTokenException
import com.birth.fit.common.exception.error.PostNotFoundException
import com.birth.fit.common.exception.error.UserNotFoundException
import com.birth.fit.common.util.JwtTokenProvider
import com.birth.fit.domain.help.dto.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

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

        helps.forEach {
            list.add(
                HelpListResponse(
                    helpId = it.id!!,
                    title = it.title,
                    comment = helpCommentRepository.countByHelpId(it.id!!),
                    like = it.likeCount
                )
            )
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
        comment?.forEach {
            val writer: User = userRepository.findByEmail(it.userEmail)!!
            list.add(
                HelpCommentResponse(
                    commentId = it.commentId!!,
                    userId = writer.userId,
                    userImage = writer.image,
                    comment = it.comment,
                    isMine = writer.email == user.email
                )
            )
        }

        helpRepository.save(help.view())
        return HelpContentResponse(
            title = help.title,
            content = help.content,
            userId = author.userId,
            userImage = author.image,
            createdAt = help.createdAt.toString(),
            view = help.view,
            likeCount = help.likeCount,
            isMine = user.email == author.email,
            isLike = helpLikeRepository.findByUserEmailAndHelpId(user.email, helpId)?.isPresent == true,
            comment = list
        )
    }

    fun write(bearerToken: String?, helpPostRequest: HelpPostRequest) {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        helpRepository.save(
            Help(
                userEmail = user.email,
                title = helpPostRequest.title,
                content = helpPostRequest.content,
                createdAt = LocalDate.now()
            )
        )
    }

    fun writeComment(bearerToken: String?, helpId: Int, helpCommentRequest: HelpCommentRequest) {
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
                comment = helpCommentRequest.comment
            )
        )
    }

    fun updateHelp(bearerToken: String?, helpId: Int, helpPostRequest: HelpPostRequest) {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val help: Help? = helpRepository.findById(helpId)
        help?: throw PostNotFoundException("Post not Found")

        helpRepository.save(help.updateContent(helpPostRequest))
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

    fun updateComment(bearerToken: String?, commentId: Int, helpCommentRequest: HelpCommentRequest) {
        val token: String? = jwtTokenProvider.resolveToken(bearerToken)
        if(!jwtTokenProvider.validateToken(token!!)) throw ExpiredTokenException("The token has expired.")

        val user: User? = userRepository.findByEmail(jwtTokenProvider.getUsername(token))
        user?: throw UserNotFoundException("User not found.")

        val comment: HelpComment? = helpCommentRepository.findByCommentId(commentId)
        comment?: throw ContentNotFoundException("Comments do not exist.")

        helpCommentRepository.save(comment.updateComment(helpCommentRequest.comment))
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