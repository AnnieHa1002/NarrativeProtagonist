package com.narrativeprotagonist.trigger.domain

import com.narrativeprotagonist._global.timestamp.Timestamped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Trigger (

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    val name: String = "",
    val description: String = "",

) : Timestamped();