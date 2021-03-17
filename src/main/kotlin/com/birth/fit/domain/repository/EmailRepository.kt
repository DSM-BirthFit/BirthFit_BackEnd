package com.birth.fit.domain.repository

import com.birth.fit.domain.entity.Email
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailRepository: CrudRepository<Email, String>