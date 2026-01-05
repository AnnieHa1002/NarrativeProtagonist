package com.narrativeprotagonist.auth.domain

import com.narrativeprotagonist._global.timestamp.Timestamped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class LoginToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var verified: Boolean = false,

    val userId: Long = 0,

    val expiresAt: Long = 0,

    var valid: Boolean = true,

) : Timestamped()