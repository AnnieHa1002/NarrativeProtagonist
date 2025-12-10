package com.narrativeprotagonist.choice

import com.narrativeprotagonist._global.timestamp.Timestamped
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

class Choice (

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    val originalNodeId: String = "",
    val text : String = "",
    @Column(columnDefinition = "jsonb")
    val conditions : String = ""
    ): Timestamped();