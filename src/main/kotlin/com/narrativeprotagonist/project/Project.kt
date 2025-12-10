package com.narrativeprotagonist.project

import com.narrativeprotagonist._global.timestamp.Timestamped
import com.narrativeprotagonist.sandbox.Sandbox
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


