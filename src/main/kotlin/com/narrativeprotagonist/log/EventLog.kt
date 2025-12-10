package com.narrativeprotagonist.log

import com.narrativeprotagonist._global.timestamp.Timestamped
import com.narrativeprotagonist.userSave.UserSave
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class EventLog (

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    val userSave : UserSave? = null,
    val nodeId : String = "",
    val occurred : Boolean = false,
) : Timestamped()