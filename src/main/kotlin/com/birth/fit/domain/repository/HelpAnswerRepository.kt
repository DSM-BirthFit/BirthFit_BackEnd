package com.birth.fit.domain.repository

import com.birth.fit.domain.entity.HelpAnswer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HelpAnswerRepository: JpaRepository<HelpAnswer, Int> {
    fun countByHelpId(helpId: Int): Int?
}