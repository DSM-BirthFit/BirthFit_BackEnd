package com.birth.fit.controller

import com.birth.fit.dto.PostRequest
import com.birth.fit.dto.QnaPageResponse
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
                @PageableDefault(size = 10) pageable: Pageable): QnaPageResponse? {
        return qnaService.getList(bearerToken, pageable)
    }

    @GetMapping("/{qnaId}")
    fun getContent(@RequestHeader("Authorization") bearerToken: String?,
                   @PathVariable @Validated qnaId: Int) {
        qnaService.getContent(bearerToken, qnaId)
    }

    @PostMapping
    fun write(@RequestHeader("Authorization") bearerToken: String?,
              @RequestBody @Validated postRequest: PostRequest) {
        qnaService.write(bearerToken, postRequest)
    }
}