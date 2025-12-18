package com.narrativeprotagonist.sandbox.service

import com.narrativeprotagonist._global.exception.BusinessException
import com.narrativeprotagonist.sandbox.domain.Sandbox
import com.narrativeprotagonist.sandbox.dto.SandboxResponse
import com.narrativeprotagonist.sandbox.repository.SandboxRepository
import com.narrativeprotagonist.user.domain.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SandboxService(
    private val sandboxRepository: SandboxRepository
) {
    fun getSandboxById(sandboxId: String): Sandbox {
        return sandboxRepository.findById(sandboxId).orElseThrow {
            BusinessException.SandboxNotFound(sandboxId)
        }
    }

    fun getUserSandbox(userId: String): SandboxResponse {
        val sandbox = sandboxRepository.findByUserId(userId)
            ?: throw BusinessException.UserSandboxNotFound(userId)
        return SandboxResponse(
            id = sandbox.id!!,
            userId = sandbox.userId!!,
            title = sandbox.title
        )

    }

    fun getSandboxList(user: User): List<SandboxResponse> {
        val sandboxes = sandboxRepository.findAllByUserId(user.id!!)
        return sandboxes.map {
            SandboxResponse(
                id = it.id!!,
                userId = it.userId!!,
                title = it.title
            )
        }
    }

    fun getSandboxByUserId(userId: String): Sandbox? {
        return sandboxRepository.findByUserId(userId)
    }

    fun createSandboxForUser(user: User) {
        sandboxRepository.save(Sandbox(user))
    }
}
