package com.narrativeprotagonist.auth.service

import com.narrativeprotagonist._global.enums.SignInStatusType
import com.narrativeprotagonist._global.exception.BusinessException
import com.narrativeprotagonist.auth.dto.SignInRequest
import com.narrativeprotagonist.auth.dto.SignInRequestResponse
import com.narrativeprotagonist.auth.dto.SignInStatus
import com.narrativeprotagonist.auth.dto.SignUpRequest
import com.narrativeprotagonist.auth.dto.SignUpResponse
import com.narrativeprotagonist.auth.dto.VerifyLoginResponse
import com.narrativeprotagonist.email.EmailService
import com.narrativeprotagonist.sandbox.service.SandboxService
import com.narrativeprotagonist.user.domain.LoginToken
import com.narrativeprotagonist.user.repository.LoginTokenRepository
import com.narrativeprotagonist.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class AuthService(
    private val userService: UserService,
    private val emailService: EmailService,
    private val sandboxService: SandboxService,
) {
    fun signUp(signUpRequest: SignUpRequest): SignUpResponse {
        val user = userService.createUser(signUpRequest)
        val expiredAt = emailService.sendVerificationEmail(user.email)
        val response = SignUpResponse(
            id = user.id!!,
            email = user.email,
            expiredAt = expiredAt
        )
        return response
    }

    fun verifyEmail(email: String, expiredAt: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        if (currentTime <= expiredAt) {
            val user = userService.getUserByEmail(email)
                ?: throw BusinessException.UserNotFound(email)
            user.verified = true
            sandboxService.createSandboxForUser(user)
            return true
        } else {
            throw BusinessException.VerificationExpired(email)
        }
    }

}