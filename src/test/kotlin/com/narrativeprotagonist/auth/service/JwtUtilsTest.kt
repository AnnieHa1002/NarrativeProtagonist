package com.narrativeprotagonist.auth.service

import com.narrativeprotagonist._global.config.JwtProperties
import com.narrativeprotagonist._global.enums.JwtTokenType
import com.narrativeprotagonist._global.exception.BusinessException
import com.narrativeprotagonist.user.domain.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

/**
 * JwtUtils 단위 테스트
 *
 * JWT 토큰 생성, 검증, 정보 추출 등의 기능을 테스트합니다.
 */
class JwtUtilsTest {

    private lateinit var jwtUtils: JwtUtils
    private lateinit var testUser: User

    @BeforeEach
    fun setUp() {
        // 테스트용 JWT 설정
        val jwtProperties = JwtProperties(
            accessSecret = "test-access-secret-key-must-be-at-least-256-bits-long-for-hs256",
            refreshSecret = "test-refresh-secret-key-must-be-at-least-256-bits-long-for-hs256",
            expiration = 3600000L, // 1시간
            refreshExpiration = 86400000L // 24시간
        )
        jwtUtils = JwtUtils(jwtProperties)

        // 테스트용 사용자 생성
        testUser = User(
            email = "test@example.com",
            nickname = "TestUser"
        ).apply {
            id = "test-user-id-123"
        }
    }

    @Test
    @DisplayName("Access Token 생성 테스트")
    fun `should generate valid access token`() {
        // Given
        val now = Date()

        // When
        val token = jwtUtils.generateJwtToken(testUser, now, JwtTokenType.ACCESS)

        // Then
        assertNotNull(token)
        assertTrue(token.isNotEmpty())
        assertEquals(3, token.split(".").size)
    }

    @Test
    @DisplayName("Refresh Token 생성 테스트")
    fun `should generate valid refresh token`() {
        // Given
        val now = Date()

        // When
        val token = jwtUtils.generateJwtToken(testUser, now, JwtTokenType.REFRESH)

        // Then
        assertNotNull(token)
        assertTrue(token.isNotEmpty())
        assertEquals(3, token.split(".").size)
    }

    @Test
    @DisplayName("유효한 Access Token 검증 테스트")
    fun `should validate access token successfully`() {
        // Given
        val token = jwtUtils.generateJwtToken(testUser, Date(), JwtTokenType.ACCESS)

        // When
        val isValid = jwtUtils.validateToken(token, JwtTokenType.ACCESS)

        // Then
        assertTrue(isValid)
    }

    @Test
    @DisplayName("유효한 Refresh Token 검증 테스트")
    fun `should validate refresh token successfully`() {
        // Given
        val token = jwtUtils.generateJwtToken(testUser, Date(), JwtTokenType.REFRESH)

        // When
        val isValid = jwtUtils.validateToken(token, JwtTokenType.REFRESH)

        // Then
        assertTrue(isValid)
    }

    @Test
    @DisplayName("잘못된 토큰 검증 실패 테스트")
    fun `should fail to validate invalid token`() {
        // Given
        val invalidToken = "invalid.token.here"

        // When
        val isValid = jwtUtils.validateToken(invalidToken, JwtTokenType.ACCESS)

        // Then
        assertFalse(isValid)
    }

    @Test
    @DisplayName("만료된 Access Token 검증 시 예외 발생")
    fun `should throw exception for expired access token`() {
        // Given - 이미 만료된 시간으로 토큰 생성
        val expiredDate = Date(System.currentTimeMillis() - 3600000L) // 1시간 전
        val jwtProperties = JwtProperties(
            accessSecret = "test-access-secret-key-must-be-at-least-256-bits-long-for-hs256",
            refreshSecret = "test-refresh-secret-key-must-be-at-least-256-bits-long-for-hs256",
            expiration = -1000L, // 음수로 설정하여 즉시 만료
            refreshExpiration = 86400000L
        )
        val expiredJwtUtils = JwtUtils(jwtProperties)
        val expiredToken = expiredJwtUtils.generateJwtToken(testUser, expiredDate, JwtTokenType.ACCESS)

        // When & Then
        assertThrows(BusinessException.AccessTokenExpired::class.java) {
            expiredJwtUtils.validateToken(expiredToken, JwtTokenType.ACCESS)
        }
    }

    @Test
    @DisplayName("Access Token에서 userId 추출 테스트")
    fun `should extract userId from access token`() {
        // Given
        val token = jwtUtils.generateJwtToken(testUser, Date(), JwtTokenType.ACCESS)

        // When
        val userId = jwtUtils.getUserIdFromToken(token, JwtTokenType.ACCESS)

        // Then
        assertEquals("test-user-id-123", userId)
    }

    @Test
    @DisplayName("Refresh Token에서 userId 추출 테스트")
    fun `should extract userId from refresh token`() {
        // Given
        val token = jwtUtils.generateJwtToken(testUser, Date(), JwtTokenType.REFRESH)

        // When
        val userId = jwtUtils.getUserIdFromToken(token, JwtTokenType.REFRESH)

        // Then
        assertEquals("test-user-id-123", userId)
    }

    @Test
    @DisplayName("토큰에서 만료 시간 추출 테스트")
    fun `should extract expiration date from token`() {
        // Given
        val now = Date()
        val token = jwtUtils.generateJwtToken(testUser, now, JwtTokenType.ACCESS)

        // When
        val expiration = jwtUtils.getExpirationFromToken(token, JwtTokenType.ACCESS)

        // Then
        assertNotNull(expiration)
        // 만료 시간이 현재 시간보다 미래인지 확인
        assertTrue(expiration.after(now))
    }

    @Test
    @DisplayName("토큰 해싱(SHA-256) 테스트")
    fun `should hash token with SHA-256`() {
        // Given
        val token = "sample-token-to-hash"

        // When
        val hash1 = jwtUtils.hashToken(token)
        val hash2 = jwtUtils.hashToken(token)

        // Then
        assertNotNull(hash1)
        assertEquals(64, hash1.length) // SHA-256은 64자 hex 문자열
        // 같은 입력은 항상 같은 해시 생성
        assertEquals(hash1, hash2)
    }

    @Test
    @DisplayName("다른 토큰은 다른 해시 생성")
    fun `should generate different hashes for different tokens`() {
        // Given
        val token1 = "token-one"
        val token2 = "token-two"

        // When
        val hash1 = jwtUtils.hashToken(token1)
        val hash2 = jwtUtils.hashToken(token2)

        // Then
        assertNotEquals(hash1, hash2)
    }

    @Test
    @DisplayName("Access Token과 Refresh Token은 다른 시크릿 키 사용")
    fun `should use different secret keys for access and refresh tokens`() {
        // Given
        val now = Date()
        val accessToken = jwtUtils.generateJwtToken(testUser, now, JwtTokenType.ACCESS)
        val refreshToken = jwtUtils.generateJwtToken(testUser, now, JwtTokenType.REFRESH)

        // When & Then
        // Access Token을 Refresh Secret으로 검증하면 실패
        assertFalse(jwtUtils.validateToken(accessToken, JwtTokenType.REFRESH))

        // Refresh Token을 Access Secret으로 검증하면 실패
        assertFalse(jwtUtils.validateToken(refreshToken, JwtTokenType.ACCESS))

        // 각각 올바른 타입으로 검증하면 성공
        assertTrue(jwtUtils.validateToken(accessToken, JwtTokenType.ACCESS))
        assertTrue(jwtUtils.validateToken(refreshToken, JwtTokenType.REFRESH))
    }
}
