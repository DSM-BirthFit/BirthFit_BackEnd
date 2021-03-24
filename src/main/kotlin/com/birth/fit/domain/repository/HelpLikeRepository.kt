package com.birth.fit.domain.repository

import com.birth.fit.domain.entity.HelpLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HelpLikeRepository: JpaRepository<HelpLike, String> {
    fun countByHelpId(helpId: Int): Int?
}