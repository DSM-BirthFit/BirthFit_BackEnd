package com.birth.fit.domain.help.dto

class HelpCommentResponse(
    val commentId: Int,
    val userId: String,
    val userImage: String?,
    val comment: String,
    val isMine: Boolean
)