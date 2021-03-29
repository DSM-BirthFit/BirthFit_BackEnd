package com.birth.fit.controller

import com.birth.fit.dto.PostRequest
import com.birth.fit.dto.QnaListResponse
import com.birth.fit.service.QnaService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/qna")
class QnaController(
    private val qnaService: QnaService
) {


    @GetMapping
    fun getList(@RequestHeader("Authorization") bearerToken: String?,
                @PageableDefault(size = 10) pageable: Pageable): MutableList<QnaListResponse>? {
        return qnaService.getList(bearerToken, pageable)
    }

    @PostMapping
    fun write(@RequestHeader("Authorization") bearerToken: String?,
              @RequestBody @Validated postRequest: PostRequest) {
        qnaService.write(bearerToken, postRequest)
    }
}