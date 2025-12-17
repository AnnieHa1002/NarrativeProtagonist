package com.narrativeprotagonist.auth.domain

import com.narrativeprotagonist._global.timestamp.Timestamped
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.*

@Entity
class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    val userId: String = "",
    val sessionId: String = "",
    val tokenHash: String = "",
    val expiresAt: Date = Date(),
    val replacedBy: String? = null,
    var revoked: Boolean = false,

// Tokens with the same userId and sessionId represent the same login session.
// A user may have multiple sessions, but each session must maintain at most one valid refresh token.


) : Timestamped() {
    constructor(session_id: String, userId: String, tokenHash: String, expiresAt: Date) : this(
        userId = userId,
        tokenHash = tokenHash,
        sessionId = session_id,
        expiresAt = expiresAt,
        replacedBy = null,
        revoked = false
    )

    constructor(currentRefreshToken: RefreshToken, tokenHash: String, expiresAt: Date, replacedBy: String?) : this(
        userId = currentRefreshToken.userId,
        sessionId = currentRefreshToken.sessionId,
        tokenHash = tokenHash,
        expiresAt = expiresAt,
        replacedBy = replacedBy,
        revoked = false
    )

    fun revoke() {
        this.revoked = true
    }
}