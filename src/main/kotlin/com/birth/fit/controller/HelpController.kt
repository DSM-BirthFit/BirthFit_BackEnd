package com.birth.fit.controller

import com.birth.fit.dto.PostRequest
import com.birth.fit.service.HelpService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/help")
class HelpController(
    @Autowired val helpService: HelpService
) {

    @PostMapping
    fun write(@RequestHeader("Authorization") bearerToken: String?,
                 @RequestBody @Validated postRequest: PostRequest) {
        return helpService.write(bearerToken, postRequest)
    }
}