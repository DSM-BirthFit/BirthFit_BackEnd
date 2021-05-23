package com.birth.fit.domain.qna.dto

class QnaAnswerResponse(
    val userId: String,
    val userImage: String?,
    val answer: String,
    val isMine: Boolean
)