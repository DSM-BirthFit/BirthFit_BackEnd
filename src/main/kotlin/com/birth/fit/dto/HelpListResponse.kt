package com.birth.fit.dto

class HelpListResponse(
    val helpId: Int,
    val title: String,
    val userEmail: String,
    val answer: Int?,
    val like: Int?
)