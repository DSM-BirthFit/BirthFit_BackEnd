package com.birth.fit.controller

import com.birth.fit.dto.ContentRequest
import com.birth.fit.dto.PostRequest
import com.birth.fit.dto.QnaContentResponse
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
                   @PathVariable @Validated qnaId: Int): QnaContentResponse {
        return qnaService.getContent(bearerToken, qnaId)
    }

    @PostMapping
    fun write(@RequestHeader("Authorization") bearerToken: String?,
              @RequestBody @Validated postRequest: PostRequest) {
        qnaService.write(bearerToken, postRequest)
    }

    @PostMapping("/answer/{qnaId}")
    fun writeAnswer(@RequestHeader("Authorization") bearerToken: String?,
                    @PathVariable @Validated qnaId: Int,
                    @RequestBody @Validated contentRequest: ContentRequest) {
        qnaService.writeAnswer(bearerToken, qnaId, contentRequest)
    }

    @PutMapping("/{qnaId}")
    fun updateQna(@RequestHeader("Authorization") bearerToken: String?,
                  @PathVariable @Validated qnaId: Int,
                  @RequestBody @Validated postRequest: PostRequest) {
        qnaService.updateQna(bearerToken, qnaId, postRequest)
    }

    @PutMapping("/{qnaId}/like")
    fun like(@RequestHeader("Authorization") bearerToken: String?,
             @PathVariable @Validated qnaId: Int) {
        qnaService.like(bearerToken, qnaId)
    }

    @PutMapping("/answer/{answerId}")
    fun updateAnswer(@RequestHeader("Authorization") bearerToken: String?,
                     @PathVariable @Validated answerId: Int,
                     @RequestBody @Validated contentRequest: ContentRequest) {
        qnaService.updateAnswer(bearerToken, answerId, contentRequest)
    }

    @DeleteMapping("/{qnaId}")
    fun deleteQna(@RequestHeader("Authorization") bearerToken: String?,
                  @PathVariable @Validated qnaId: Int) {
        qnaService.deleteQna(bearerToken, qnaId)
    }
}