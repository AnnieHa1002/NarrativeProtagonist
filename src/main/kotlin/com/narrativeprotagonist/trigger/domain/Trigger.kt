package com.narrativeprotagonist.trigger.domain

import com.narrativeprotagonist._global.timestamp.Timestamped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Trigger (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    val name: String = "",
    val description: String = "",

) : Timestamped();