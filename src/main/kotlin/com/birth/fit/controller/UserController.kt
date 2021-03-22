package com.birth.fit.controller

import com.birth.fit.dto.ChangePasswordRequest
import com.birth.fit.dto.JoinRequest
import com.birth.fit.dto.LoginRequest
import com.birth.fit.dto.TokenResponse
import com.birth.fit.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

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

    @PutMapping("/password")
    fun findPassword(@RequestBody @Validated changePasswordRequest: ChangePasswordRequest) {
        userService.findPassword(changePasswordRequest)
    }
}