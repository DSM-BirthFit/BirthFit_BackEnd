package com.birth.fit.domain.qna.controller

import com.birth.fit.domain.qna.dto.QnaAnswerRequest
import com.birth.fit.domain.qna.dto.QnaContentResponse
import com.birth.fit.domain.qna.dto.QnaPageResponse
import com.birth.fit.domain.qna.dto.QnaPostRequest
import com.birth.fit.domain.qna.service.QnaService
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
              @RequestBody @Validated qnaPostRequest: QnaPostRequest) {
        qnaService.write(bearerToken, qnaPostRequest)
    }

    @PostMapping("/answer/{qnaId}")
    fun writeAnswer(@RequestHeader("Authorization") bearerToken: String?,
                    @PathVariable @Validated qnaId: Int,
                    @RequestBody @Validated qnaAnswerRequest: QnaAnswerRequest) {
        qnaService.writeAnswer(bearerToken, qnaId, qnaAnswerRequest)
    }

    @PutMapping("/{qnaId}")
    fun updateQna(@RequestHeader("Authorization") bearerToken: String?,
                  @PathVariable @Validated qnaId: Int,
                  @RequestBody @Validated qnaPostRequest: QnaPostRequest) {
        qnaService.updateQna(bearerToken, qnaId, qnaPostRequest)
    }

    @PutMapping("/{qnaId}/like")
    fun like(@RequestHeader("Authorization") bearerToken: String?,
             @PathVariable @Validated qnaId: Int) {
        qnaService.like(bearerToken, qnaId)
    }

    @PutMapping("/answer/{answerId}")
    fun updateAnswer(@RequestHeader("Authorization") bearerToken: String?,
                     @PathVariable @Validated answerId: Int,
                     @RequestBody @Validated qnaAnswerRequest: QnaAnswerRequest) {
        qnaService.updateAnswer(bearerToken, answerId, qnaAnswerRequest)
    }

    @DeleteMapping("/{qnaId}")
    fun deleteQna(@RequestHeader("Authorization") bearerToken: String?,
                  @PathVariable @Validated qnaId: Int) {
        qnaService.deleteQna(bearerToken, qnaId)
    }

    @DeleteMapping("/answer/{answerId}")
    fun deleteAnswer(@RequestHeader("Authorization") bearerToken: String?,
                     @PathVariable @Validated answerId: Int) {
        qnaService.deleteAnswer(bearerToken, answerId)
    }
}