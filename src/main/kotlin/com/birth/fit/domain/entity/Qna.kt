package com.birth.fit.domain.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "qna")
class Qna(
    @Id
    @Column(name = "qna_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    internal val id: Int? = null,

    @Column(name = "user_email")
    internal val userEmail: String,

    @Column(name = "title")
    internal var title: String,

    @Column(name = "content")
    internal var content: String,

    @Column(name = "created_at")
    internal val createdAt: LocalDateTime,

    @Column(name = "view")
    internal var view: Int = 0,

    @Column(name = "like_count")
    internal var likeCount: Int = 0,

    @OneToMany(mappedBy = "qnaId")
    private val answer: MutableList<QnaAnswer>? = null,

    @OneToMany(mappedBy = "qnaId")
    private val likes: MutableList<QnaLike>? = null
)