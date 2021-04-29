package com.birth.fit.domain.email.domain.entity

import com.birth.fit.domain.email.domain.enums.EmailVerificationStatus
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash(timeToLive = 60 * 3)
class Email(

    @Id
    private val email: String,
    internal val code: String,
    private var status: EmailVerificationStatus,

    @TimeToLive
    private var ttl: Long? = 0,
    private val MINUTE: Long = 60L
) {

    fun verify(): Email {
        status = EmailVerificationStatus.VERIFIED
        ttl = 3 * MINUTE
        return this
    }

    fun isVerified(): Boolean {
        return status == EmailVerificationStatus.VERIFIED
    }
}