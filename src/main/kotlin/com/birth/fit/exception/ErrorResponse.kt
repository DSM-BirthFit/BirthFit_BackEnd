package com.birth.fit.exception

import java.util.*

class ErrorResponse(
    val timestamp: Date,
    val code: Int,
    val status: String,
    val message: String
)