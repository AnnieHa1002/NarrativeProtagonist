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
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    val user: User? = null,
    val projectId : String? = null,
    val currentNodeId: String? = null,
    @Column(columnDefinition = "jsonb")
    val variables : String? = null,
    @Column(columnDefinition = "jsonb")
    val items : String? = null,
    @Column(columnDefinition = "jsonb")
    val history : String? = null,

) : Timestamped()
