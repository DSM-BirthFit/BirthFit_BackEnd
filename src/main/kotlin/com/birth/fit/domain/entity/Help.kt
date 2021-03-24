package com.birth.fit.domain.entity

import com.birth.fit.dto.PostRequest
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "help")
class Help (

    @Id
    @Column(name = "help_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    internal var id: Int? = null,

    @Column(name = "user_email")
    internal val userEmail: String,

    @Column(name = "title")
    internal var title: String,

    @Column(name = "content")
    internal var content: String,

    @Column(name = "created_at")
    internal val createdAt: LocalDateTime,

    @Column(name = "view")
    internal var view: Int = 0
) {

    fun view(): Help {
        view++
        return this
    }

    fun updateContent(postRequest: PostRequest): Help {
        this.title = postRequest.title
        this.content = postRequest.content
        return this
    }
}