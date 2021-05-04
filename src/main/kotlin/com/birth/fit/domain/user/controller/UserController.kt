package com.birth.fit.domain.user.controller

import com.birth.fit.domain.user.dto.*
import com.birth.fit.domain.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    @Autowired val userService: UserService
) {

    @GetMapping("/userId")
    fun checkUserId(@RequestParam("userId") userId: String): Boolean {
        return userService.checkUserId(userId)
    }

    @GetMapping("/profile")
    fun getProfile(): ProfileResponse {
        return userService.getProfile()
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
    fun refreshToken(): TokenResponse {
        return userService.refreshToken()
    }

    @PutMapping("/password")
    fun findPassword(@RequestBody @Validated changePasswordRequest: ChangePasswordRequest) {
        userService.findPassword(changePasswordRequest)
    }

    @PutMapping("/profile")
    fun changeProfile(@ModelAttribute @Validated profileRequest: ChangeProfileRequest
    ) {
        userService.changeProfile(profileRequest)
    }

    @DeleteMapping
    fun deleteUser(@RequestBody @Validated passwordRequest: PasswordRequest) {
        userService.deleteUser(passwordRequest)
    }
}