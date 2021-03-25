package com.birth.fit.dto

class HelpListResponse(
    val helpId: Int,
    val title: String,
    val userEmail: String,
    val comment: Int?,
    val like: Int?
)