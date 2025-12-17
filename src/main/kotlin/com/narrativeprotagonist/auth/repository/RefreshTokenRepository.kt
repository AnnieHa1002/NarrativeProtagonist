package com.narrativeprotagonist.auth.repository

import com.narrativeprotagonist.auth.domain.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, String> {

    @Query("SELECT rt FROM RefreshToken rt WHERE rt.tokenHash = :hash AND rt.revoked = false AND rt.expiresAt > CURRENT_TIMESTAMP")
    fun findValidByTokenHash(hash: String): RefreshToken?
}