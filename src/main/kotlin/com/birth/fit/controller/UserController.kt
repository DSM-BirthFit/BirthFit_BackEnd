package com.birth.fit.controller

import com.birth.fit.dto.*
import com.birth.fit.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    @Autowired val userService: UserService
) {

    @GetMapping("/profile")
    fun getProfile(@RequestHeader("Authorization") bearerToken: String): ProfileResponse {
        return userService.getProfile(bearerToken)
    }

    @PostMapping("/join")
    fun join(@RequestBody @Validated joinRequest: JoinRequest) {
        userService.join(joinRequest)
    }

    @PostMapping("/login")
    fun login(@RequestBody @Validated loginRequest: LoginRequest): TokenResponse {
        return userService.login(loginRequest)
    }

    @PutMapping
    fun refreshToken(@RequestHeader("X-Refresh-Token") refreshToken: String?): TokenResponse {
        return userService.refreshToken(refreshToken)
    }

    @PutMapping("/password")
    fun findPassword(@RequestBody @Validated changePasswordRequest: ChangePasswordRequest) {
        userService.findPassword(changePasswordRequest)
    }

    @PutMapping("/profile")
    fun changeProfile(
        @RequestHeader("Authorization") bearerToken: String,
        @RequestBody @Validated profileRequest: ChangeProfileRequest
    ) {
        userService.changeProfile(bearerToken, profileRequest)
    }
}