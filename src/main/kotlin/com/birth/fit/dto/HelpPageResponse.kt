package com.birth.fit.dto

class HelpPageResponse(
    val totalElement: Int,
    val totalPage: Int,
    val listResponse: MutableList<HelpListResponse>
)