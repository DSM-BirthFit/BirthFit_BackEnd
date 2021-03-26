package com.birth.fit.controller

import com.birth.fit.dto.HelpContentResponse
import com.birth.fit.dto.HelpListResponse
import com.birth.fit.dto.PostRequest
import com.birth.fit.dto.QnaListResponse
import com.birth.fit.service.QnaService
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/qna")
class QnaController(
    private val qnaService: QnaService
) {


    @GetMapping
    fun getList(@RequestHeader("Authorization") bearerToken: String?,
                pageable: Pageable): MutableList<QnaListResponse>? {
        return qnaService.getList(bearerToken, pageable)
    }

//    @GetMapping("/{helpId}")
//    fun getContent(@RequestHeader("Authorization") bearerToken: String?,
//                   @PathVariable @Validated helpId: Int): HelpContentResponse? {
//        return qnaService.getContent(bearerToken, helpId)
//    }

    @PostMapping
    fun write(@RequestHeader("Authorization") bearerToken: String?,
              @RequestBody @Validated postRequest: PostRequest) {
        qnaService.write(bearerToken, postRequest)
    }
}