package com.narrativeprotagonist.item

import com.narrativeprotagonist._global.enums.ActionType
import com.narrativeprotagonist._global.timestamp.Timestamped
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

class ItemLog(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    var itemId: String = "",
    var action: ActionType = ActionType.NONE,
    var nodeId: String = "",
) : Timestamped()