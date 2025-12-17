package com.narrativeprotagonist.sandbox.repository

import com.narrativeprotagonist.sandbox.domain.Sandbox
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SandboxRepository : JpaRepository<Sandbox, String> {

    fun findByUserId(userId: String): Sandbox?
    // TODO: 추가 쿼리 메서드 작성
}
