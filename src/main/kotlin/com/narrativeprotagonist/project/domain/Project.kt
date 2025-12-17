package com.narrativeprotagonist.project.domain

import com.narrativeprotagonist._global.timestamp.Timestamped
import com.narrativeprotagonist.sandbox.domain.Sandbox
import jakarta.persistence.*

@Entity
class Project(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    val sandbox: Sandbox? = null,
    val userId: String = "",
    val title: String = "",
    val description: String = "",
) : Timestamped()


