package com.birth.fit.domain.repository

import com.birth.fit.domain.entity.HelpLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface HelpLikeRepository: JpaRepository<HelpLike, Any> {
    fun findByHelpIdAndUserEmail(helpId: Int, userEmail: String): HelpLike?
    fun findByUserEmailAndHelpId(userEmail: String, helpId: Int): Optional<HelpLike>?
}