package com.birth.fit.domain.entity

import com.birth.fit.domain.enums.EmailVerificationStatus
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash(timeToLive = 60 * 3)
class Email(

    @Id var email: String,
    var code: String,
    var status: EmailVerificationStatus
) {
    @TimeToLive
    var ttl: Long? = 0
    val MINUTE: Long = 60L

    fun verify(): Email {
        status = EmailVerificationStatus.VERIFIED
        ttl = 3 * MINUTE
        return this
    }

    fun isVerified(): Boolean {
        return status == EmailVerificationStatus.VERIFIED
    }
}