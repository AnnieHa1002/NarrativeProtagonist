package com.narrativeprotagonist.trigger

import com.narrativeprotagonist._global.timestamp.Timestamped
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

class Trigger (

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    val name: String = "",
    val description: String = "",

) : Timestamped();