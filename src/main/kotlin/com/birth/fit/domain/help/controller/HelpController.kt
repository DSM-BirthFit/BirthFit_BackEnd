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
    fun getList(@PageableDefault(size = 10) pageable: Pageable): HelpPageResponse? {
        return helpService.getList(pageable)
    }

    @GetMapping("/{helpId}")
    fun getContent(@PathVariable @Validated helpId: Int): HelpContentResponse? {
        return helpService.getContent(helpId)
    }

    @PostMapping
    fun write(@RequestBody @Validated helpPostRequest: HelpPostRequest) {
        return helpService.write(helpPostRequest)
    }

    @PostMapping("/{helpId}/comment")
    fun writeComment(@PathVariable @Validated helpId: Int,
                     @RequestBody @Validated helpCommentRequest: HelpCommentRequest) {
        helpService.writeComment(helpId, helpCommentRequest)
    }

    @PutMapping("/{helpId}")
    fun updateHelp(@PathVariable @Validated helpId: Int,
                   @RequestBody @Validated helpPostRequest: HelpPostRequest) {
        helpService.updateHelp(helpId, helpPostRequest)
    }

    @PutMapping("/{helpId}/like")
    fun like(@PathVariable @Validated helpId: Int) {
        helpService.like(helpId)
    }

    @PutMapping("/comment/{commentId}")
    fun updateComment(@PathVariable @Validated commentId: Int,
                      @RequestBody @Validated helpCommentRequest: HelpCommentRequest) {
        helpService.updateComment(commentId, helpCommentRequest)
    }

    @DeleteMapping("/{helpId}")
    fun deleteHelp(@PathVariable @Validated helpId: Int) {
        helpService.deleteHelp(helpId)
    }

    @DeleteMapping("/comment/{commentId}")
    fun deleteComment(@PathVariable @Validated commentId: Int) {
        helpService.deleteComment(commentId)
    }
}