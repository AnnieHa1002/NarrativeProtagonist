package com.narrativeprotagonist.item.domain

import com.narrativeprotagonist._global.enums.ActionType
import com.narrativeprotagonist._global.timestamp.Timestamped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class ItemLog(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var itemId: Long = 0,
    var action: ActionType = ActionType.NONE,
    var nodeId: Long = 0,
) : Timestamped()