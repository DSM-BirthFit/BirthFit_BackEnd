package com.birth.fit.controller

import com.birth.fit.dto.EmailVerifyRequest
import com.birth.fit.service.EmailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/email")
class EmailController(
    @Autowired val emailService: EmailService
) {

    @PostMapping("/send")
    fun sendCode(@RequestParam email: String) {
        emailService.sendCode(email)
    }

    @PostMapping("/password")
    fun findPassword(@RequestParam email: String) {
        emailService.findPassword(email)
    }

    @PutMapping("/verify")
    fun verifyEmail(@RequestBody @Validated emailVerifyRequest: EmailVerifyRequest) {
        emailService.emailVerify(emailVerifyRequest)
    }
}