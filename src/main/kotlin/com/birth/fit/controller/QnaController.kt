package com.birth.fit.controller

import com.birth.fit.dto.PostRequest
import com.birth.fit.service.QnaService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/qna")
class QnaController(
    private val qnaService: QnaService
) {

    @PostMapping
    fun write(@RequestHeader("Authorization") bearerToken: String?,
              @RequestBody @Validated postRequest: PostRequest) {
        qnaService.write(bearerToken, postRequest)
    }
}