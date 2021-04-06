package com.birth.fit.dto

class QnaContentResponse(
    val title: String,
    val content: String,
    val userId: String,
    val createdAt: String,
    val view: Int,
    val likeCount: Int,
    val isMine: Boolean,
    val isLike: Boolean,
    val answer: MutableList<QnaAnswerResponse>
)