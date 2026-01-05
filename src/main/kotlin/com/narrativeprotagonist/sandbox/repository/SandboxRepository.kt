package com.narrativeprotagonist.sandbox.repository

import com.narrativeprotagonist.sandbox.domain.Sandbox
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SandboxRepository : JpaRepository<Sandbox, Long> {

    fun findByUserId(userId: Long): Sandbox?
    fun findAllByUserId(userId: Long): List<Sandbox>
}
