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
    fun getList(@PageableDefault(size = 10) pageable: Pageable): QnaPageResponse? {
        return qnaService.getList(pageable)
    }

    @GetMapping("/{qnaId}")
    fun getContent(@PathVariable @Validated qnaId: Int): QnaContentResponse {
        return qnaService.getContent(qnaId)
    }

    @PostMapping
    fun write(@RequestBody @Validated qnaPostRequest: QnaPostRequest) {
        qnaService.write(qnaPostRequest)
    }

    @PostMapping("/{qnaId}/answer")
    fun writeAnswer(@PathVariable @Validated qnaId: Int,
                    @RequestBody @Validated qnaAnswerRequest: QnaAnswerRequest) {
        qnaService.writeAnswer(qnaId, qnaAnswerRequest)
    }

    @PutMapping("/{qnaId}")
    fun updateQna(@PathVariable @Validated qnaId: Int,
                  @RequestBody @Validated qnaPostRequest: QnaPostRequest) {
        qnaService.updateQna(qnaId, qnaPostRequest)
    }

    @PutMapping("/{qnaId}/like")
    fun like(@PathVariable @Validated qnaId: Int) {
        qnaService.like(qnaId)
    }

    @PutMapping("/answer/{answerId}")
    fun updateAnswer(@PathVariable @Validated answerId: Int,
                     @RequestBody @Validated qnaAnswerRequest: QnaAnswerRequest) {
        qnaService.updateAnswer(answerId, qnaAnswerRequest)
    }

    @DeleteMapping("/{qnaId}")
    fun deleteQna(@PathVariable @Validated qnaId: Int) {
        qnaService.deleteQna(qnaId)
    }

    @DeleteMapping("/answer/{answerId}")
    fun deleteAnswer(@PathVariable @Validated answerId: Int) {
        qnaService.deleteAnswer(answerId)
    }
}