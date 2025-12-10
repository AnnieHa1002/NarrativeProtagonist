package com.narrativeprotagonist.user

import com.narrativeprotagonist._global.timestamp.Timestamped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.Email

@Entity
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,

    @Email
    val email: String

) : Timestamped()
