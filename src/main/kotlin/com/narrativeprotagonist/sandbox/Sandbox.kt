package com.narrativeprotagonist.sandbox

import com.narrativeprotagonist._global.timestamp.Timestamped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Sandbox(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,

    val userId: String = "",
    val title: String = ""


) : Timestamped()