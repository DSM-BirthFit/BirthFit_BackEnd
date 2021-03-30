package com.birth.fit.dto

import java.time.LocalDateTime

class QnaContentResponse(
    val title: String,
    val content: String,
    val userId: String,
    val createdAt: LocalDateTime,
    val view: Int,
    val like: Int? = 0,
    val isMine: Boolean,
    val isLike: Boolean,
    val answer: MutableList<QnaAnswerResponse>
)