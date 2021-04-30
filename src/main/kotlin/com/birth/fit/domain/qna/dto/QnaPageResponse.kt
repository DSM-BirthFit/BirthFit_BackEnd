package com.birth.fit.domain.qna.dto

class QnaPageResponse(
    val totalElement: Int,
    val totalPage: Int,
    val listResponse: MutableList<QnaListResponse>
)