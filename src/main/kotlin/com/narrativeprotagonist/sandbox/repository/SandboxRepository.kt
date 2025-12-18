package com.narrativeprotagonist.sandbox.repository

import com.narrativeprotagonist.sandbox.domain.Sandbox
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SandboxRepository : JpaRepository<Sandbox, String> {

    fun findByUserId(userId: String): Sandbox?
    fun findAllByUserId(userId: String): List<Sandbox>
}
