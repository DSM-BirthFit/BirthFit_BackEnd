package com.birth.fit.dto

class QnaAnswerResponse(
    val qnaId: Int,
    val userId: String,
    val content: String,
    val isMine: Boolean
)