package com.narrativeprotagonist.item

import com.narrativeprotagonist._global.timestamp.Timestamped
import com.narrativeprotagonist.project.Project
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
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    var project: Project? = null,
    var name : String = "",
    var description : String = "",

    @Column(columnDefinition = "jsonb")
    var conditions : String? = null,

    @Column(columnDefinition = "jsonb")
    var effect : String? = null,


) : Timestamped()