package com.birth.fit.domain.user.dto

class TokenResponse(
    var accessToken: String,
    var refreshToken: String,
    var tokenType: String
)