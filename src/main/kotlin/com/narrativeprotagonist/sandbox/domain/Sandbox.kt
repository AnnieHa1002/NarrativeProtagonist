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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    val userId: Long = 0,
    var title: String = ""


) : Timestamped(){
    protected constructor() : this(id = 0, userId = 0, title = "")
    constructor(user : User) : this(id = 0, userId = user.id, title = user.nickname)
}