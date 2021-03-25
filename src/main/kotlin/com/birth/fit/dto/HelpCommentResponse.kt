package com.birth.fit.dto

class HelpCommentResponse(
    val commentId: Int,
    val userId: String,
    val content: String,
    val isMine: Boolean
)