package com.narrativeprotagonist.sandbox.service

import com.narrativeprotagonist.sandbox.domain.Sandbox
import com.narrativeprotagonist.sandbox.repository.SandboxRepository
import com.narrativeprotagonist.user.domain.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SandboxService(
    private val sandboxRepository: SandboxRepository
) {
    fun createSandboxForUser(user: User) {
        sandboxRepository.save(Sandbox(user))
    }
}
