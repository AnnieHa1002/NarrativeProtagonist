package com.narrativeprotagonist.auth.service

import com.narrativeprotagonist._global.enums.JwtTokenType
import com.narrativeprotagonist._global.enums.SignInStatusType
import com.narrativeprotagonist._global.exception.BusinessException
import com.narrativeprotagonist.auth.domain.LoginToken
import com.narrativeprotagonist.auth.domain.RefreshToken
import com.narrativeprotagonist.auth.dto.SignInRequest
import com.narrativeprotagonist.auth.dto.SignUpRequest
import com.narrativeprotagonist.auth.repository.LoginTokenRepository
import com.narrativeprotagonist.auth.repository.RefreshTokenRepository
import com.narrativeprotagonist.email.EmailService
import com.narrativeprotagonist.sandbox.service.SandboxService
import com.narrativeprotagonist.user.domain.User
import com.narrativeprotagonist.user.service.UserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.time.LocalDateTime
import java.util.*

/**
 * AuthService 단위 테스트
 *
 * Mock을 사용하여 의존성을 격리하고 AuthService의 비즈니스 로직만 테스트합니다.
 */
@ExtendWith(MockitoExtension::class)
class AuthServiceTest {

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var emailService: EmailService

    @Mock
    private lateinit var sandboxService: SandboxService

    @Mock
    private lateinit var jwtUtils: JwtUtils

    @Mock
    private lateinit var loginTokenRepository: LoginTokenRepository

    @Mock
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @InjectMocks
    private lateinit var authService: AuthService

    private lateinit var testUser: User

    @BeforeEach
    fun setUp() {
        testUser = User(
            email = "test@example.com",
            nickname = "TestUser"
        ).apply {
            id = "test-user-id"
            verified = true
        }
    }

    // ============ 회원가입 테스트 ============

    @Test
    @DisplayName("회원가입 성공")
    fun `should sign up user successfully`() {
        // Given
        val signUpRequest = SignUpRequest(
            email = "test@example.com",
            nickname = "TestUser"
        )
        val expiredAt = System.currentTimeMillis() + 600000L

        whenever(userService.createUser(signUpRequest)).thenReturn(testUser)
        whenever(emailService.sendVerificationEmail(eq(testUser.email), any())).thenReturn(expiredAt)

        // When
        val response = authService.signUp(signUpRequest)

        // Then
        assertNotNull(response)
        assertEquals("test-user-id", response.id)
        assertEquals("test@example.com", response.email)
        assertEquals(expiredAt, response.expiredAt)

        verify(userService, times(1)).createUser(signUpRequest)
        verify(emailService, times(1)).sendVerificationEmail(eq(testUser.email), any())
    }

    // ============ 이메일 인증 테스트 ============

    @Test
    @DisplayName("이메일 인증 성공")
    fun `should verify email successfully`() {
        // Given
        val email = "test@example.com"
        val expiredAt = System.currentTimeMillis() + 60000L
        testUser.verified = false

        whenever(userService.getUserByEmail(email)).thenReturn(testUser)
        doNothing().whenever(sandboxService).createSandboxForUser(testUser)

        // When
        val result = authService.verifyEmail(email, expiredAt)

        // Then
        assertTrue(result)
        assertTrue(testUser.verified)
        verify(userService, times(1)).getUserByEmail(email)
        verify(sandboxService, times(1)).createSandboxForUser(testUser)
    }

    @Test
    @DisplayName("이메일 인증 - 만료된 링크로 실패")
    fun `should fail to verify email with expired link`() {
        // Given
        val email = "test@example.com"
        val expiredAt = System.currentTimeMillis() - 60000L // 이미 만료됨

        // When & Then
        val exception = assertThrows(BusinessException.VerificationExpired::class.java) {
            authService.verifyEmail(email, expiredAt)
        }

        assertEquals(email, exception.email)
        verify(userService, never()).getUserByEmail(any())
        verify(sandboxService, never()).createSandboxForUser(any())
    }

    @Test
    @DisplayName("이메일 인증 - 존재하지 않는 사용자")
    fun `should fail to verify email for non-existent user`() {
        // Given
        val email = "nonexistent@example.com"
        val expiredAt = System.currentTimeMillis() + 60000L

        whenever(userService.getUserByEmail(email)).thenReturn(null)

        // When & Then
        assertThrows(BusinessException.UserNotFound::class.java) {
            authService.verifyEmail(email, expiredAt)
        }

        verify(userService, times(1)).getUserByEmail(email)
        verify(sandboxService, never()).createSandboxForUser(any())
    }

    // ============ 로그인 요청 테스트 ============

    @Test
    @DisplayName("로그인 요청 성공 - 매직 링크 발송")
    fun `should request login and send magic link successfully`() {
        // Given
        val request = SignInRequest(email = "test@example.com")
        val loginToken = LoginToken(
            userId = testUser.id!!,
            expiresAt = System.currentTimeMillis() + 600000L
        ).apply {
            id = "login-token-id"
        }

        whenever(userService.getUserByEmail(request.email)).thenReturn(testUser)
        whenever(loginTokenRepository.findLatestByUserId(testUser.id!!)).thenReturn(null)
        whenever(loginTokenRepository.save(any<LoginToken>())).thenAnswer { invocation ->
            val token = invocation.arguments[0] as LoginToken
            token.id = "login-token-id"
            token
        }
        doNothing().whenever(loginTokenRepository).invalidateAllByUserId(testUser.id!!)
        doNothing().whenever(emailService).sendSignInEmail(any(), any(), any())

        // When
        val response = authService.requestLogin(request)

        // Then
        assertNotNull(response)
        assertEquals("login-token-id", response.loginTokenId)

        verify(userService, times(1)).getUserByEmail(request.email)
        verify(loginTokenRepository, times(1)).invalidateAllByUserId(testUser.id!!)
        verify(loginTokenRepository, times(1)).save(any<LoginToken>())
        verify(emailService, times(1)).sendSignInEmail(any(), any(), any())
    }

    @Test
    @DisplayName("로그인 요청 실패 - 인증되지 않은 사용자")
    fun `should fail to request login for unverified user`() {
        // Given
        val request = SignInRequest(email = "test@example.com")
        testUser.verified = false

        whenever(userService.getUserByEmail(request.email)).thenReturn(testUser)

        // When & Then
        assertThrows(BusinessException.UserNotVerified::class.java) {
            authService.requestLogin(request)
        }

        verify(userService, times(1)).getUserByEmail(request.email)
        verify(loginTokenRepository, never()).save(any())
        verify(emailService, never()).sendSignInEmail(any(), any(), any())
    }

    @Test
    @DisplayName("로그인 요청 실패 - 1분 이내 재전송 시도")
    fun `should fail to request login within 1 minute cooldown`() {
        // Given
        val request = SignInRequest(email = "test@example.com")
        val recentToken = LoginToken(
            userId = testUser.id!!,
            expiresAt = System.currentTimeMillis() + 600000L
        ).apply {
            // 30초 전에 생성된 토큰
            createdAt = LocalDateTime.now().minusSeconds(30)
        }

        whenever(userService.getUserByEmail(request.email)).thenReturn(testUser)
        whenever(loginTokenRepository.findLatestByUserId(testUser.id!!)).thenReturn(recentToken)

        // When & Then
        assertThrows(BusinessException.TooManyLoginRequests::class.java) {
            authService.requestLogin(request)
        }

        verify(userService, times(1)).getUserByEmail(request.email)
        verify(loginTokenRepository, never()).save(any())
        verify(emailService, never()).sendSignInEmail(any(), any(), any())
    }

    // ============ 로그인 상태 확인 테스트 ============

    @Test
    @DisplayName("로그인 상태 확인 - PENDING (아직 이메일 미클릭)")
    fun `should return pending status when login not verified yet`() {
        // Given
        val loginTokenId = "login-token-id"
        val loginToken = LoginToken(
            userId = testUser.id!!,
            expiresAt = System.currentTimeMillis() + 600000L
        ).apply {
            verified = false
        }

        whenever(loginTokenRepository.findById(loginTokenId)).thenReturn(Optional.of(loginToken))

        // When
        val status = authService.checkLoginStatus(loginTokenId)

        // Then
        assertEquals(SignInStatusType.PENDING, status.status)
        assertNull(status.tokens)

        verify(loginTokenRepository, times(1)).findById(loginTokenId)
    }

    @Test
    @DisplayName("로그인 상태 확인 - SUCCESS (이메일 클릭 완료)")
    fun `should return success status with tokens when login verified`() {
        // Given
        val loginTokenId = "login-token-id"
        val loginToken = LoginToken(
            userId = testUser.id!!,
            expiresAt = System.currentTimeMillis() + 600000L
        ).apply {
            verified = true
        }
        val accessToken = "access-token"
        val refreshToken = "refresh-token"

        whenever(loginTokenRepository.findById(loginTokenId)).thenReturn(Optional.of(loginToken))
        whenever(userService.getUserById(testUser.id!!)).thenReturn(testUser)
        whenever(jwtUtils.generateJwtToken(eq(testUser), any(), eq(JwtTokenType.ACCESS)))
            .thenReturn(accessToken)
        whenever(jwtUtils.generateJwtToken(eq(testUser), any(), eq(JwtTokenType.REFRESH)))
            .thenReturn(refreshToken)
        whenever(jwtUtils.hashToken(refreshToken)).thenReturn("hashed-refresh-token")
        whenever(jwtUtils.getExpirationFromToken(refreshToken, JwtTokenType.REFRESH))
            .thenReturn(Date(System.currentTimeMillis() + 86400000L))
        whenever(refreshTokenRepository.save(any<RefreshToken>()))
            .thenAnswer { it.arguments[0] }

        // When
        val status = authService.checkLoginStatus(loginTokenId)

        // Then
        assertEquals(SignInStatusType.SUCCESS, status.status)
        assertNotNull(status.tokens)
        assertEquals(accessToken, status.tokens!!.accessToken)
        assertEquals(refreshToken, status.tokens!!.refreshToken)

        verify(loginTokenRepository, times(1)).findById(loginTokenId)
        verify(userService, times(1)).getUserById(testUser.id!!)
        verify(refreshTokenRepository, times(1)).save(any<RefreshToken>())
    }

    @Test
    @DisplayName("로그인 상태 확인 실패 - 만료된 토큰")
    fun `should fail to check login status with expired token`() {
        // Given
        val loginTokenId = "login-token-id"
        val loginToken = LoginToken(
            userId = testUser.id!!,
            expiresAt = System.currentTimeMillis() - 1000L // 이미 만료됨
        )

        whenever(loginTokenRepository.findById(loginTokenId)).thenReturn(Optional.of(loginToken))

        // When & Then
        assertThrows(BusinessException.TokenExpired::class.java) {
            authService.checkLoginStatus(loginTokenId)
        }

        verify(loginTokenRepository, times(1)).findById(loginTokenId)
    }

    // ============ 로그인 토큰 검증 테스트 ============

    @Test
    @DisplayName("로그인 토큰 검증 성공")
    fun `should verify login token successfully`() {
        // Given
        val tokenId = "login-token-id"
        val loginToken = LoginToken(
            userId = testUser.id!!,
            expiresAt = System.currentTimeMillis() + 600000L
        ).apply {
            verified = false
            valid = true
        }

        whenever(loginTokenRepository.findById(tokenId)).thenReturn(Optional.of(loginToken))
        whenever(loginTokenRepository.save(any<LoginToken>())).thenReturn(loginToken)

        // When
        val response = authService.verifyLoginToken(tokenId)

        // Then
        assertTrue(response.success)
        assertTrue(loginToken.verified)
        assertFalse(loginToken.valid)

        verify(loginTokenRepository, times(1)).findById(tokenId)
        verify(loginTokenRepository, times(1)).save(loginToken)
    }

    @Test
    @DisplayName("로그인 토큰 검증 실패 - 이미 사용된 토큰")
    fun `should fail to verify already used login token`() {
        // Given
        val tokenId = "login-token-id"
        val loginToken = LoginToken(
            userId = testUser.id!!,
            expiresAt = System.currentTimeMillis() + 600000L
        ).apply {
            valid = false
        }

        whenever(loginTokenRepository.findById(tokenId)).thenReturn(Optional.of(loginToken))

        // When & Then
        assertThrows(BusinessException.InvalidToken::class.java) {
            authService.verifyLoginToken(tokenId)
        }

        verify(loginTokenRepository, times(1)).findById(tokenId)
        verify(loginTokenRepository, never()).save(any())
    }

    // ============ 토큰 재발급 테스트 ============

    @Test
    @DisplayName("리프레시 토큰으로 새 토큰 발급 성공")
    fun `should reissue tokens successfully with valid refresh token`() {
        // Given
        val oldRefreshToken = "old-refresh-token"
        val tokenHash = "hashed-refresh-token"
        val newAccessToken = "new-access-token"
        val newRefreshToken = "new-refresh-token"

        val oldRefreshTokenEntity = RefreshToken(
            sessionId = "session-id",
            userId = testUser.id!!,
            tokenHash = tokenHash,
            expiresAt = Date(System.currentTimeMillis() + 86400000L)
        )

        whenever(jwtUtils.validateToken(oldRefreshToken, JwtTokenType.REFRESH)).thenReturn(true)
        whenever(jwtUtils.hashToken(oldRefreshToken)).thenReturn(tokenHash)
        whenever(refreshTokenRepository.findValidByTokenHash(tokenHash)).thenReturn(oldRefreshTokenEntity)
        whenever(userService.getUserById(testUser.id!!)).thenReturn(testUser)
        whenever(jwtUtils.generateJwtToken(eq(testUser), any(), eq(JwtTokenType.ACCESS)))
            .thenReturn(newAccessToken)
        whenever(jwtUtils.generateJwtToken(eq(testUser), any(), eq(JwtTokenType.REFRESH)))
            .thenReturn(newRefreshToken)
        whenever(jwtUtils.hashToken(newRefreshToken)).thenReturn("new-hashed-token")
        whenever(jwtUtils.getExpirationFromToken(newRefreshToken, JwtTokenType.REFRESH))
            .thenReturn(Date(System.currentTimeMillis() + 86400000L))
        whenever(refreshTokenRepository.save(any<RefreshToken>())).thenAnswer { it.arguments[0] }

        // When
        val tokens = authService.reissueTokensRequest(oldRefreshToken)

        // Then
        assertNotNull(tokens)
        assertEquals(newAccessToken, tokens.accessToken)
        assertEquals(newRefreshToken, tokens.refreshToken)

        verify(jwtUtils, times(1)).validateToken(oldRefreshToken, JwtTokenType.REFRESH)
        verify(refreshTokenRepository, times(1)).save(any<RefreshToken>())
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 유효하지 않은 리프레시 토큰")
    fun `should fail to reissue tokens with invalid refresh token`() {
        // Given
        val invalidRefreshToken = "invalid-refresh-token"

        whenever(jwtUtils.validateToken(invalidRefreshToken, JwtTokenType.REFRESH)).thenReturn(false)

        // When & Then
        assertThrows(BusinessException.InvalidToken::class.java) {
            authService.reissueTokensRequest(invalidRefreshToken)
        }

        verify(jwtUtils, times(1)).validateToken(invalidRefreshToken, JwtTokenType.REFRESH)
        verify(refreshTokenRepository, never()).findValidByTokenHash(any())
    }
}
