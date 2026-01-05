package com.narrativeprotagonist.userSave.domain

import com.narrativeprotagonist._global.timestamp.Timestamped
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class EventLog (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    val userSave : UserSave? = null,
    val nodeId : Long = 0,
    val occurred : Boolean = false,
) : Timestamped()