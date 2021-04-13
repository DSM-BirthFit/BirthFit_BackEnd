package com.birth.fit.dto

class HelpCommentResponse(
    val commentId: Int,
    val userId: String,
    val userImage: String?,
    val content: String,
    val isMine: Boolean
)