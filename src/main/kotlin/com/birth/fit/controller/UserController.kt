package com.birth.fit.controller

import com.birth.fit.dto.JoinRequest
import com.birth.fit.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    val userService: UserService
) {

    @PostMapping("/join")
    fun join(@RequestBody joinRequest: JoinRequest) {
        userService.join(joinRequest)
    }
}