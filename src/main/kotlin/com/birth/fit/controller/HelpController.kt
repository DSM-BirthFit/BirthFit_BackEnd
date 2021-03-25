package com.birth.fit.controller

import com.birth.fit.dto.HelpContentResponse
import com.birth.fit.dto.HelpListResponse
import com.birth.fit.dto.PostCommentRequest
import com.birth.fit.dto.PostRequest
import com.birth.fit.service.HelpService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.GetMapping

@RestController
@RequestMapping("/help")
class HelpController(
    @Autowired val helpService: HelpService
) {

    @GetMapping
    fun getList(@RequestHeader("Authorization") bearerToken: String?,
                     pageable: Pageable): MutableList<HelpListResponse>? {
        return helpService.getList(bearerToken, pageable)
    }

    @GetMapping("/{helpId}")
    fun getContent(@RequestHeader("Authorization") bearerToken: String?,
                   @PathVariable @Validated helpId: Int): HelpContentResponse? {
        return helpService.getContent(bearerToken, helpId)
    }

    @PostMapping
    fun write(@RequestHeader("Authorization") bearerToken: String?,
              @RequestBody @Validated postRequest: PostRequest) {
        return helpService.write(bearerToken, postRequest)
    }

    @PostMapping("/{helpId}/comment")
    fun writeComment(@RequestHeader("Authorization") bearerToken: String?,
                     @PathVariable @Validated helpId: Int,
                     @RequestBody @Validated postCommentRequest: PostCommentRequest) {
        helpService.writeComment(bearerToken, helpId, postCommentRequest)
    }

    @PutMapping("/{helpId}")
    fun updateHelp(@RequestHeader("Authorization") bearerToken: String?,
                   @PathVariable @Validated helpId: Int,
                   @RequestBody @Validated postRequest: PostRequest) {
        helpService.updateHelp(bearerToken, helpId, postRequest)
    }

    @PutMapping("/{helpId}/like")
    fun like(@RequestHeader("Authorization") bearerToken: String?,
             @PathVariable @Validated helpId: Int) {
        helpService.like(bearerToken, helpId)
    }

    @DeleteMapping("/{helpId}")
    fun deleteHelp(@RequestHeader("Authorization") bearerToken: String?,
                   @PathVariable @Validated helpId: Int) {
        helpService.deleteHelp(bearerToken, helpId)
    }
}