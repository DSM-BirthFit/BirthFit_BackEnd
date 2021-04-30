package com.birth.fit.domain.qna.dto

class QnaListResponse(
    val qnaId: Int,
    val title: String,
    val answer: Int?,
    val like: Int?
)