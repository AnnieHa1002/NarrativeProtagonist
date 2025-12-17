package com.narrativeprotagonist.auth.domain

import com.narrativeprotagonist._global.timestamp.Timestamped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class LoginToken(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,

    var verified: Boolean = false,

    val userId: String = "",

    val expiresAt: Long = 0,

    var used: Boolean = false,

) : Timestamped()