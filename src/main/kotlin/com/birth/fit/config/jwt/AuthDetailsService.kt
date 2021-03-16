package com.birth.fit.config.jwt

import com.birth.fit.domain.entity.User
import com.birth.fit.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class AuthDetailsService(
    @Autowired private val userRepository: UserRepository
) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): AuthDetails {
        val user: User? = userRepository.findByEmail(username)
        user?: throw UsernameNotFoundException("cannot find such username : $username")

        return AuthDetails(user.email, user.password)
    }
}