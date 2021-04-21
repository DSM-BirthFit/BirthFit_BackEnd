package com.birth.fit.domain.help.controller

import com.birth.fit.domain.help.dto.HelpCommentRequest
import com.birth.fit.domain.help.dto.HelpContentResponse
import com.birth.fit.domain.help.dto.HelpPageResponse
import com.birth.fit.domain.help.dto.HelpPostRequest
import com.birth.fit.domain.help.service.HelpService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
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
                @PageableDefault(size = 10) pageable: Pageable): HelpPageResponse? {
        return helpService.getList(bearerToken, pageable)
    }

    @GetMapping("/{helpId}")
    fun getContent(@RequestHeader("Authorization") bearerToken: String?,
                   @PathVariable @Validated helpId: Int): HelpContentResponse? {
        return helpService.getContent(bearerToken, helpId)
    }

    @PostMapping
    fun write(@RequestHeader("Authorization") bearerToken: String?,
              @RequestBody @Validated helpPostRequest: HelpPostRequest) {
        return helpService.write(bearerToken, helpPostRequest)
    }

    @PostMapping("/comment/{helpId}")
    fun writeComment(@RequestHeader("Authorization") bearerToken: String?,
                     @PathVariable @Validated helpId: Int,
                     @RequestBody @Validated helpCommentRequest: HelpCommentRequest) {
        helpService.writeComment(bearerToken, helpId, helpCommentRequest)
    }

    @PutMapping("/{helpId}")
    fun updateHelp(@RequestHeader("Authorization") bearerToken: String?,
                   @PathVariable @Validated helpId: Int,
                   @RequestBody @Validated helpPostRequest: HelpPostRequest) {
        helpService.updateHelp(bearerToken, helpId, helpPostRequest)
    }

    @PutMapping("/{helpId}/like")
    fun like(@RequestHeader("Authorization") bearerToken: String?,
             @PathVariable @Validated helpId: Int) {
        helpService.like(bearerToken, helpId)
    }

    @PutMapping("/comment/{commentId}")
    fun updateComment(@RequestHeader("Authorization") bearerToken: String?,
                      @PathVariable @Validated commentId: Int,
                      @RequestBody @Validated helpCommentRequest: HelpCommentRequest) {
        helpService.updateComment(bearerToken, commentId, helpCommentRequest)
    }

    @DeleteMapping("/{helpId}")
    fun deleteHelp(@RequestHeader("Authorization") bearerToken: String?,
                   @PathVariable @Validated helpId: Int) {
        helpService.deleteHelp(bearerToken, helpId)
    }

    @DeleteMapping("/comment/{commentId}")
    fun deleteComment(@RequestHeader("Authorization") bearerToken: String?,
                      @PathVariable @Validated commentId: Int) {
        helpService.deleteComment(bearerToken, commentId)
    }
}