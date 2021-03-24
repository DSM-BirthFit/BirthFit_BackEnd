package com.birth.fit.controller

import com.birth.fit.dto.HelpListResponse
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
    fun getBoardList(@RequestHeader("Authorization") bearerToken: String?,
                     pageable: Pageable): MutableList<HelpListResponse>? {
        return helpService.getList(bearerToken, pageable)
    }

    @PostMapping
    fun write(@RequestHeader("Authorization") bearerToken: String?,
              @RequestBody @Validated postRequest: PostRequest) {
        return helpService.write(bearerToken, postRequest)
    }

    @PutMapping("/{helpId}")
    fun updateHelp(@RequestHeader("Authorization") bearerToken: String?,
                   @PathVariable @Validated helpId: Int,
                   @RequestBody @Validated postRequest: PostRequest) {
        helpService.updateHelp(bearerToken, helpId, postRequest)
    }
}