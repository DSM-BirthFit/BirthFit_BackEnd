package com.birth.fit.domain.user.dto

import org.springframework.web.multipart.MultipartFile

class ChangeProfileRequest(
    var userId: String?,
    var password: String?,
    var image: MultipartFile?
)