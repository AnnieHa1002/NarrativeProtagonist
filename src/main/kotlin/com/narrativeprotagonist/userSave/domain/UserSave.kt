package com.narrativeprotagonist.userSave.domain

import com.narrativeprotagonist._global.timestamp.Timestamped
import com.narrativeprotagonist.user.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class UserSave (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    val user: User? = null,
    val projectId : Long? = null,
    val currentNodeId: Long? = null,
    @Column(columnDefinition = "jsonb")
    val variables : String? = null,
    @Column(columnDefinition = "jsonb")
    val items : String? = null,
    @Column(columnDefinition = "jsonb")
    val history : String? = null,

) : Timestamped()
