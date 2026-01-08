package com.narrativeprotagonist.sandbox.service

import com.narrativeprotagonist._global.exception.BusinessException
import com.narrativeprotagonist.sandbox.domain.Sandbox
import com.narrativeprotagonist.sandbox.repository.SandboxRepository
import com.narrativeprotagonist.user.domain.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.util.*

/**
 * SandboxService 단위 테스트
 *
 * Mock을 사용하여 의존성을 격리하고 SandboxService의 비즈니스 로직만 테스트합니다.
 */
@ExtendWith(MockitoExtension::class)
class SandboxServiceTest {

    @Mock
    private lateinit var sandboxRepository: SandboxRepository

    @InjectMocks
    private lateinit var sandboxService: SandboxService

    private lateinit var testUser: User
    private lateinit var testSandbox: Sandbox

    @BeforeEach
    fun setUp() {
        testUser = User(
            email = "test@example.com",
            nickname = "TestUser"
        ).apply {
            id = 1L
            verified = true
        }

        testSandbox = Sandbox(
            userId = testUser.id,
            title = "Test Sandbox"
        ).apply {
            id = 1L
        }
    }

    // ============ ID로 샌드박스 조회 테스트 ============

    @Test
    @DisplayName("ID로 샌드박스 조회 성공")
    fun `should get sandbox by ID successfully`() {
        // Given
        val sandboxId = 1L

        whenever(sandboxRepository.findById(sandboxId))
            .thenReturn(Optional.of(testSandbox))

        // When
        val result = sandboxService.getSandboxById(sandboxId)

        // Then
        assertNotNull(result)
        assertEquals(1L, result.id)
        assertEquals(testUser.id, result.userId)

        verify(sandboxRepository, times(1)).findById(sandboxId)
    }

    @Test
    @DisplayName("ID로 샌드박스 조회 실패 - 존재하지 않는 샌드박스")
    fun `should fail to get sandbox by ID when not found`() {
        // Given
        val sandboxId = 999L

        whenever(sandboxRepository.findById(sandboxId))
            .thenReturn(Optional.empty())

        // When & Then
        assertThrows(BusinessException.SandboxNotFound::class.java) {
            sandboxService.getSandboxById(sandboxId)
        }

        verify(sandboxRepository, times(1)).findById(sandboxId)
    }

    // ============ 사용자 샌드박스 조회 테스트 ============

    @Test
    @DisplayName("사용자 샌드박스 조회 성공")
    fun `should get user sandbox successfully`() {
        // Given
        val userId = testUser.id!!

        whenever(sandboxRepository.findByUserId(userId))
            .thenReturn(testSandbox)

        // When
        val result = sandboxService.getUserSandbox(userId)

        // Then
        assertNotNull(result)
        assertEquals(1L, result.id)
        assertEquals(userId, result.userId)
        assertEquals("Test Sandbox", result.title)

        verify(sandboxRepository, times(1)).findByUserId(userId)
    }

    @Test
    @DisplayName("사용자 샌드박스 조회 실패 - 샌드박스 없음")
    fun `should fail to get user sandbox when not found`() {
        // Given
        val userId = 999L

        whenever(sandboxRepository.findByUserId(userId))
            .thenReturn(null)

        // When & Then
        assertThrows(BusinessException.UserSandboxNotFound::class.java) {
            sandboxService.getUserSandbox(userId)
        }

        verify(sandboxRepository, times(1)).findByUserId(userId)
    }

    // ============ 샌드박스 목록 조회 테스트 ============

    @Test
    @DisplayName("샌드박스 목록 조회 성공")
    fun `should get sandbox list successfully`() {
        // Given
        val sandboxes = listOf(
            testSandbox,
            Sandbox(
                userId = testUser.id,
                title = "Second Sandbox"
            ).apply {
                id = 2L
            }
        )

        whenever(sandboxRepository.findAllByUserId(testUser.id!!))
            .thenReturn(sandboxes)

        // When
        val result = sandboxService.getSandboxList(testUser.id!!)

        // Then
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals(1L, result[0].id)
        assertEquals(2L, result[1].id)
        assertEquals("Test Sandbox", result[0].title)
        assertEquals("Second Sandbox", result[1].title)

        verify(sandboxRepository, times(1)).findAllByUserId(testUser.id!!)
    }

    @Test
    @DisplayName("샌드박스 목록 조회 - 빈 목록 반환")
    fun `should return empty list when user has no sandboxes`() {
        // Given
        whenever(sandboxRepository.findAllByUserId(testUser.id!!))
            .thenReturn(emptyList())

        // When
        val result = sandboxService.getSandboxList(testUser.id!!)

        // Then
        assertNotNull(result)
        assertTrue(result.isEmpty())

        verify(sandboxRepository, times(1)).findAllByUserId(testUser.id!!)
    }

    // ============ 사용자 ID로 샌드박스 조회 테스트 ============

    @Test
    @DisplayName("사용자 ID로 샌드박스 조회 성공")
    fun `should get sandbox by user ID successfully`() {
        // Given
        val userId = testUser.id!!

        whenever(sandboxRepository.findByUserId(userId))
            .thenReturn(testSandbox)

        // When
        val result = sandboxService.getSandboxByUserId(userId)

        // Then
        assertNotNull(result)
        assertEquals(1L, result?.id)
        assertEquals(userId, result?.userId)

        verify(sandboxRepository, times(1)).findByUserId(userId)
    }

    @Test
    @DisplayName("사용자 ID로 샌드박스 조회 - null 반환")
    fun `should return null when user has no sandbox`() {
        // Given
        val userId = 999L

        whenever(sandboxRepository.findByUserId(userId))
            .thenReturn(null)

        // When
        val result = sandboxService.getSandboxByUserId(userId)

        // Then
        assertNull(result)

        verify(sandboxRepository, times(1)).findByUserId(userId)
    }

    // ============ 사용자를 위한 샌드박스 생성 테스트 ============

    @Test
    @DisplayName("사용자를 위한 샌드박스 생성 성공")
    fun `should create sandbox for user successfully`() {
        // Given
        whenever(sandboxRepository.save(any<Sandbox>()))
            .thenAnswer { it.arguments[0] }

        // When
        sandboxService.createSandboxForUser(testUser)

        // Then
        verify(sandboxRepository, times(1)).save(any<Sandbox>())
    }

    @Test
    @DisplayName("사용자를 위한 샌드박스 생성 - 사용자 닉네임이 타이틀로 설정됨")
    fun `should create sandbox with user nickname as title`() {
        // Given
        val capturedSandbox = argumentCaptor<Sandbox>()

        whenever(sandboxRepository.save(capturedSandbox.capture()))
            .thenAnswer { it.arguments[0] }

        // When
        sandboxService.createSandboxForUser(testUser)

        // Then
        val savedSandbox = capturedSandbox.firstValue
        assertEquals(testUser.id, savedSandbox.userId)
        assertEquals(testUser.nickname, savedSandbox.title)

        verify(sandboxRepository, times(1)).save(any<Sandbox>())
    }
}