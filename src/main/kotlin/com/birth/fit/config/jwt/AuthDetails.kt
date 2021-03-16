package com.birth.fit.config.jwt

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AuthDetails(
    var USERNAME: String,
    var PASSWORD: String
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val auth: ArrayList<GrantedAuthority> = ArrayList()
        return auth
    }

    override fun getPassword(): String = PASSWORD

    override fun getUsername(): String = USERNAME

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}