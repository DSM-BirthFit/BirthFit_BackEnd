package com.birth.fit.domain.email.service

import com.birth.fit.domain.email.domain.entity.Email
import com.birth.fit.domain.user.domain.entity.User
import com.birth.fit.domain.email.domain.enums.EmailVerificationStatus
import com.birth.fit.domain.email.domain.repository.EmailRepository
import com.birth.fit.domain.user.domain.repository.UserRepository
import com.birth.fit.domain.email.dto.EmailVerifyRequest
import com.birth.fit.common.exception.error.InvalidAuthCodeException
import com.birth.fit.common.exception.error.InvalidAuthEmailException
import com.birth.fit.common.exception.error.UserAlreadyExistException
import com.birth.fit.common.exception.error.UserNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*


@Service
class EmailService(
    @Value("\${spring.mail.username}")
    val adminEmail: String,
    @Autowired val userRepository: UserRepository,
    @Autowired val emailRepository: EmailRepository,
    @Autowired val javaMailSender: JavaMailSender
) {

    fun sendCode(email: String) {
        val user: User? = userRepository.findByEmail(email)
        user?.let { throw UserAlreadyExistException("This email already exists.") }

        val code: String = randomCode()

        this.sendEmail(email, code)

        emailRepository.save(
            Email(
                email,
                code,
                EmailVerificationStatus.UNVERIFIED
            )
        )
    }

    fun findPassword(email: String) {
        val user: User? = userRepository.findByEmail(email)
        user?: throw UserNotFoundException("Unsubscribed email.")

        val code: String = randomCode()

        this.sendEmail(email, code)

        emailRepository.save(
            Email(
                email,
                code,
                EmailVerificationStatus.UNVERIFIED
            )
        )
    }

    fun emailVerify(request: EmailVerifyRequest) {
        val email:  String = request.email
        val code: String = request.code

        val data: Email = emailRepository.findById(email)
            .orElseThrow {
                InvalidAuthEmailException("This email did not request authentication.")
            }

        if (data.code != code) throw InvalidAuthCodeException("The authentication number does not match.")

        emailRepository.save(data.verify())
    }

    @Async
    fun sendEmail(email: String?, code: String) {
        val mailMessage = SimpleMailMessage()
        mailMessage.setFrom(adminEmail)
        mailMessage.setTo(email)
        mailMessage.setSubject("BirthFit ????????? ???????????????.")
        mailMessage.setText("?????? ????????? ?????? ????????? " + code + "?????????.")
        javaMailSender.send(mailMessage)
    }

    private fun randomCode(): String {
        val codes = arrayOf(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
        )
        val random = Random(System.currentTimeMillis())
        val tableLength = codes.size
        val buf = StringBuffer()
        for (i in 0..5) {
            buf.append(codes[random.nextInt(tableLength)])
        }
        return buf.toString()
    }
}