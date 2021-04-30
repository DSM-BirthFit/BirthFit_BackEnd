package com.birth.fit.domain.user.dto

import org.springframework.web.multipart.MultipartFile

class ChangeProfileRequest(
    val userId: String,
    val password: String?,
    val image: MultipartFile?
)