package com.narrativeprotagonist.project.domain

import com.narrativeprotagonist._global.timestamp.Timestamped
import com.narrativeprotagonist.sandbox.domain.Sandbox
import jakarta.persistence.*

@Entity
class Project(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    val sandbox: Sandbox? = null,
    val userId: Long = 0,
    val title: String = "",
    val description: String = "",
) : Timestamped()


