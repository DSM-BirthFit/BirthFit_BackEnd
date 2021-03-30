package com.birth.fit.dto

class QnaPageResponse(
    val totalElement: Int,
    val totalPage: Int,
    val listResponse: MutableList<QnaListResponse>
)