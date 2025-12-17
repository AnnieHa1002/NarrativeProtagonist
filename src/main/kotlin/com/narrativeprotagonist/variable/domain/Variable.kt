package com.narrativeprotagonist.variable.domain

import com.narrativeprotagonist._global.timestamp.Timestamped
import com.narrativeprotagonist.project.domain.Project
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class Variable (

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,


    @ManyToOne(fetch = FetchType.LAZY)
    var project: Project? = null,
    var key : String = "",
    @Column(columnDefinition = "jsonb")
    var value : String = "",
) : Timestamped()