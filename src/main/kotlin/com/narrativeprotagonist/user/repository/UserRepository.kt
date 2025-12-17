package com.narrativeprotagonist.user.repository

import com.narrativeprotagonist.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, String> {
    fun findByEmail(email: String): User?  // Optional 대신 nullable 타입 사용
    fun existsByEmail(email: String): Boolean
}