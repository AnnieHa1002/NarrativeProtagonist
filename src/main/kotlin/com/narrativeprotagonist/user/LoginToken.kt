package com.narrativeprotagonist.user

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
class LoginToken {

    @Id
    val token: String = ""

    val userId: String = ""

    val expiresAt: Long = 0

    val used: Boolean = false


}