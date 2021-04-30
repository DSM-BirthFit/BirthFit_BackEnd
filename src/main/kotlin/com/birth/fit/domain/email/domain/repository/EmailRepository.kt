package com.birth.fit.domain.email.domain.repository

import com.birth.fit.domain.email.domain.entity.Email
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailRepository: CrudRepository<Email, String>