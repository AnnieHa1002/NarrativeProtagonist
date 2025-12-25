package com.narrativeprotagonist.auth.repository

import com.narrativeprotagonist.auth.domain.LoginToken
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface LoginTokenRepository : JpaRepository<LoginToken, String> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE login_token SET valid = false WHERE user_id = :userId", nativeQuery = true)
    fun invalidateAllByUserId(userId: String)

    fun findLatestByUserId(userId: String): LoginToken?

    fun findByIdAndValidIsTrue(id: String): LoginToken?
}

