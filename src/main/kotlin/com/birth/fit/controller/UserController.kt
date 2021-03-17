package com.birth.fit.controller

import com.birth.fit.dto.JoinRequest
import com.birth.fit.dto.LoginRequest
import com.birth.fit.dto.TokenResponse
import com.birth.fit.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    @Autowired val userService: UserService
) {

    @PostMapping("/join")
    fun join(@RequestBody @Validated joinRequest: JoinRequest) {
        userService.join(joinRequest)
    }

    @PostMapping("/login")
    fun login(@RequestBody @Validated loginRequest: LoginRequest): TokenResponse {
        return userService.login(loginRequest)
    }
}