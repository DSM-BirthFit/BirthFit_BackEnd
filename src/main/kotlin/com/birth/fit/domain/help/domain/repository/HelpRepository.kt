package com.birth.fit.domain.help.domain.repository

import com.birth.fit.domain.help.domain.entity.Help
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HelpRepository: JpaRepository<Help, String> {
    fun findById(id: Int): Help?
}