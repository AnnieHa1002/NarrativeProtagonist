package com.narrativeprotagonist.item.domain

import com.narrativeprotagonist._global.timestamp.Timestamped
import com.narrativeprotagonist.project.domain.Project
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.sql.Time

@Entity
class Item  (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    var project: Project? = null,
    var name : String = "",
    var description : String = "",
    var count : Int? ,



) : Timestamped()