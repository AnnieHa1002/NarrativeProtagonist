package com.narrativeprotagonist.sandbox.domain

import com.narrativeprotagonist._global.timestamp.Timestamped
import com.narrativeprotagonist.user.domain.User
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Sandbox(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    val userId: String? = "",
    var title: String = ""


) : Timestamped(){
    protected constructor() : this(id = null, userId = "", title = "")
    constructor(user : User) : this(id = null, userId = user.id, title = user.nickname)
}