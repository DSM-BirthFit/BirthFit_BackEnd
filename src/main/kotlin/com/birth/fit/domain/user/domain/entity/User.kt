package com.birth.fit.domain.user.domain.entity

import com.birth.fit.domain.help.domain.entity.Help
import com.birth.fit.domain.help.domain.entity.HelpComment
import com.birth.fit.domain.help.domain.entity.HelpLike
import com.birth.fit.domain.qna.domain.entity.Qna
import com.birth.fit.domain.qna.domain.entity.QnaAnswer
import com.birth.fit.domain.qna.domain.entity.QnaLike
import javax.persistence.*

@Entity
@Table(name = "user")
class User(

    @Id
    @Column(name = "email")
    internal val email: String,

    @Column(name = "user_id")
    internal var userId: String,

    @Column(name = "password")
    internal var password: String,

    @Column(name = "image")
    internal var image: String? = null,

    @OneToMany(mappedBy = "userEmail")
    private val help: MutableList<Help>? = null,

    @OneToMany(mappedBy = "userEmail")
    private val helpLike: MutableList<HelpLike>? = null,

    @OneToMany(mappedBy = "userEmail")
    private val comment: MutableList<HelpComment>? = null,

    @OneToMany(mappedBy = "userEmail")
    private val qna: MutableList<Qna>? = null,

    @OneToMany(mappedBy = "userEmail")
    private val qnaLike: MutableList<QnaLike>? = null,

    @OneToMany(mappedBy = "userEmail")
    private val answer: MutableList<QnaAnswer>? = null
)