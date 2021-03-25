package com.birth.fit.domain.repository

import com.birth.fit.domain.entity.HelpComment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HelpCommentRepository: JpaRepository<HelpComment, Int>