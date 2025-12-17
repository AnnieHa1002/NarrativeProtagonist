package com.narrativeprotagonist.auth.repository

import com.narrativeprotagonist.auth.domain.LoginToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface LoginTokenRepository : JpaRepository<LoginToken, String> {

    @Query(value = "select * from login_token where userId = :userId", nativeQuery = true)
    fun invalidateAllByUserId(userId: String)

    fun findLatestByUserId(userId: String): LoginToken?
}

