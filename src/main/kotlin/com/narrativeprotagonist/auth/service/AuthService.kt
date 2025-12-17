package com.narrativeprotagonist.auth.service

import com.narrativeprotagonist._global.enums.JwtTokenType
import com.narrativeprotagonist._global.enums.SignInStatusType
import com.narrativeprotagonist._global.exception.BusinessException
import com.narrativeprotagonist.auth.domain.LoginToken
import com.narrativeprotagonist.auth.domain.RefreshToken
import com.narrativeprotagonist.auth.dto.*
import com.narrativeprotagonist.auth.repository.LoginTokenRepository
import com.narrativeprotagonist.auth.repository.RefreshTokenRepository
import com.narrativeprotagonist.email.EmailService
import com.narrativeprotagonist.sandbox.service.SandboxService
import com.narrativeprotagonist.user.domain.User
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
    private val jwtUtils: JwtUtils,
    private val loginTokenRepository: LoginTokenRepository,
    private val refreshTokenRepository: RefreshTokenRepository
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

    /**
     * Step 1: 로그인 요청 - 이메일로 매직 링크 발송
     */
    fun requestLogin(request: SignInRequest): SignInRequestResponse {
        val email = request.email
        val user = userService.getUserByEmail(email)
            ?: throw BusinessException.UserNotFound(email)
        if (!user.verified) {
            throw BusinessException.UserNotVerified(email)
        }
        checkIfMailSentIn1min(user.id!!)

        // LoginToken 생성 (10분 만료)
        val token = UUID.randomUUID().toString()
        val loginToken = LoginToken(
            userId = user.id!!,
            expiresAt = System.currentTimeMillis() + 10 * 60 * 1000  // 10분
        )
        loginTokenRepository.invalidateAllByUserId(user.id!!)
        loginTokenRepository.save(loginToken)

        // 이메일 발송
        emailService.sendSignInEmail(user.email, user.locale, token)

        return SignInRequestResponse(
            loginTokenId = loginToken.id!!,
        )
    }

    /**
     * 이메일 재전송 제한 확인 (1분 이내 재전송 불가)
     */
    fun checkIfMailSentIn1min(userId: String) {
        val now = System.currentTimeMillis()
        val cooldownMillis = 60 * 1000 // 1분

        val latestToken = loginTokenRepository
            .findLatestByUserId(userId)
        val cooldown = latestToken?.createdAtEpochMilli()?.let { now - it }
        if (cooldown != null) {
            if (cooldown < cooldownMillis) {
                throw BusinessException.TooManyLoginRequests(
                    retryAfterSeconds = ((cooldownMillis - (cooldown)) / 1000)
                )
            }
        }
    }

    /**
     * Step 2: 로그인 상태 확인 (Polling)
     * 프론트엔드에서 2초마다 호출
     */
    fun checkLoginStatus(loginTokenId: String): SignInStatus {
        val loginToken = loginTokenRepository.findById(loginTokenId).orElse(null)
            ?: throw BusinessException.InvalidToken("LoginToken not found")

        // 만료 확인
        if (System.currentTimeMillis() > loginToken.expiresAt) {
            throw BusinessException.TokenExpired
        }

        return if (loginToken.verified) {
            val user = userService.getUserById(loginToken.userId)
            SignInStatus(
                status = SignInStatusType.SUCCESS,
                tokens = generateTokenForUser(user)
            )
        } else {
            SignInStatus(
                status = SignInStatusType.PENDING,
                tokens = null,
            )
        }
    }

    private fun generateTokenForUser(user: User): JwtTokens {
        val now = Date()
        val sessionId = UUID.randomUUID().toString()

        val accessToken = jwtUtils.generateJwtToken(user, now, JwtTokenType.ACCESS)
        val refreshToken = jwtUtils.generateJwtToken(user, now, JwtTokenType.REFRESH)
        refreshTokenRepository.save(
            RefreshToken(
                sessionId = sessionId,
                userId = user.id!!,
                tokenHash = jwtUtils.hashToken(refreshToken),  
                expiresAt = jwtUtils.getExpirationFromToken(refreshToken, JwtTokenType.REFRESH),
            )
        )
        return JwtTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    /**
     * Step 3: 이메일 링크 클릭 시 호출
     * LoginToken을 검증 완료 상태로 변경
     */
    fun verifyLoginToken(id: String): VerifyLoginResponse {
        val loginToken = loginTokenRepository.findById(id).orElse(null)
            ?: throw BusinessException.InvalidToken("LoginToken not found")
        if (System.currentTimeMillis() > loginToken.expiresAt) {
            throw BusinessException.TokenExpired
        }

        // 이미 사용됨
        if (loginToken.used) {
            throw BusinessException.InvalidToken("Token already used")
        }

        // 검증 완료 표시
        loginToken.verified = true
        loginToken.used = true
        loginTokenRepository.save(loginToken)

        return VerifyLoginResponse(
            success = true,
        )
    }

    fun reissueTokensRequest(oldRefreshToken: String): JwtTokens {
        if (!jwtUtils.validateToken(oldRefreshToken, JwtTokenType.REFRESH)) {
            throw BusinessException.InvalidToken("Invalid Refresh Token")
        }
        return reissueTokens(oldRefreshToken)
    }


    private fun reissueTokens(oldRefreshToken: String): JwtTokens {
        val hash = jwtUtils.hashToken(oldRefreshToken)  // SHA-256 해시 사용
        val old = refreshTokenRepository.findValidByTokenHash(hash)
            ?: throw BusinessException.InvalidToken("Valid RefreshToken not found")
        old.revoke()

        val user = userService.getUserById(old.userId)
        val now = Date()

        val access = jwtUtils.generateJwtToken(user, now, JwtTokenType.ACCESS)
        val refresh = jwtUtils.generateJwtToken(user, now, JwtTokenType.REFRESH)

        refreshTokenRepository.save(
            RefreshToken(
                sessionId = old.sessionId,
                userId = old.userId,
                tokenHash = jwtUtils.hashToken(refresh),
                expiresAt = jwtUtils.getExpirationFromToken(refresh, JwtTokenType.REFRESH),
            )
        )

        return JwtTokens(
            accessToken = access,
            refreshToken = refresh,
        )
    }
}