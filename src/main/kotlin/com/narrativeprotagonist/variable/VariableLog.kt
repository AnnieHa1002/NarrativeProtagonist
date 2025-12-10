package com.narrativeprotagonist.variable

import com.narrativeprotagonist._global.timestamp.Timestamped
import com.narrativeprotagonist.project.Project
import com.narrativeprotagonist.userSave.UserSave
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class VariableLog (

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,


    @ManyToOne(fetch = FetchType.LAZY)
    var userSave: UserSave? = null,
    var key : String = "",
    @Column(columnDefinition = "jsonb")
    var oldValue : String = "",
    @Column(columnDefinition = "jsonb")
    var newValue : String = "",
    var nodeId : String = "",
) : Timestamped()