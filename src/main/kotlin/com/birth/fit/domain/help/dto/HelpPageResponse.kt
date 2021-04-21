package com.birth.fit.domain.help.dto

class HelpPageResponse(
    val totalElement: Int,
    val totalPage: Int,
    val listResponse: MutableList<HelpListResponse>
)