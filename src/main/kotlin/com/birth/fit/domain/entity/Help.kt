package com.birth.fit.domain.entity

import com.birth.fit.dto.PostRequest
import java.time.LocalDate
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
    internal val createdAt: LocalDate,

    @Column(name = "view")
    internal var view: Int = 0,

    @Column(name = "like_count")
    internal var likeCount: Int = 0,

    @OneToMany(mappedBy = "helpId")
    private val comment: MutableList<HelpComment>? = null,

    @OneToMany(mappedBy = "helpId")
    private val likes: MutableList<HelpLike>? = null
) {

    fun view(): Help {
        view++
        return this
    }

    fun like(): Help {
        likeCount++
        return this
    }

    fun unLike(): Help {
        likeCount--
        return this
    }

    fun updateContent(postRequest: PostRequest): Help {
        this.title = postRequest.title
        this.content = postRequest.content
        return this
    }
}