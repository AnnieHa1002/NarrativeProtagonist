package com.narrativeprotagonist.project.service

import com.narrativeprotagonist._global.exception.BusinessException
import com.narrativeprotagonist.project.domain.Project
import com.narrativeprotagonist.project.dto.ProjectCreateRequest
import com.narrativeprotagonist.project.repository.ProjectReleaseRepository
import com.narrativeprotagonist.project.repository.ProjectRepository
import com.narrativeprotagonist.sandbox.domain.Sandbox
import com.narrativeprotagonist.sandbox.service.SandboxService
import com.narrativeprotagonist.user.domain.User
import org.hibernate.query.SortDirection
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.util.*

/**
 * ProjectService 단위 테스트
 *
 * Mock을 사용하여 의존성을 격리하고 ProjectService의 비즈니스 로직만 테스트합니다.
 */
@ExtendWith(MockitoExtension::class)
class ProjectServiceTest {

    @Mock
    private lateinit var projectRepository: ProjectRepository

    @Mock
    private lateinit var projectReleaseRepository: ProjectReleaseRepository

    @Mock
    private lateinit var sandboxService: SandboxService

    @InjectMocks
    private lateinit var projectService: ProjectService

    private lateinit var testUser: User
    private lateinit var testSandbox: Sandbox
    private lateinit var testProject: Project

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

        testProject = Project(
            sandbox = testSandbox,
            userId = testUser.id!!,
            title = "Test Project",
            description = "Test Description"
        ).apply {
            id = 1L
        }
    }

    // ============ 프로젝트 목록 조회 테스트 ============

    @Test
    @DisplayName("프로젝트 목록 조회 성공 - 내림차순 정렬")
    fun `should get project list successfully with descending order`() {
        // Given
        val sandboxId = 1L
        val projects = listOf(testProject)
        val pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending())
        val page = PageImpl(projects, pageable, 1)

        whenever(projectRepository.findBySandboxId(eq(sandboxId), any()))
            .thenReturn(page)

        // When
        val result = projectService.getProjectList(sandboxId, 10, 0, "createdAt", SortDirection.DESCENDING)

        // Then
        assertNotNull(result)
        assertEquals(1, result.totalElements)
        assertEquals(1L, result.content[0].id)
        assertEquals("Test Project", result.content[0].title)

        verify(projectRepository, times(1)).findBySandboxId(eq(sandboxId), any())
    }

    @Test
    @DisplayName("프로젝트 목록 조회 성공 - 오름차순 정렬")
    fun `should get project list successfully with ascending order`() {
        // Given
        val sandboxId = 1L
        val projects = listOf(testProject)
        val pageable = PageRequest.of(0, 10, Sort.by("title").ascending())
        val page = PageImpl(projects, pageable, 1)

        whenever(projectRepository.findBySandboxId(eq(sandboxId), any()))
            .thenReturn(page)

        // When
        val result = projectService.getProjectList(sandboxId, 10, 0, "title", SortDirection.ASCENDING)

        // Then
        assertNotNull(result)
        assertEquals(1, result.totalElements)

        verify(projectRepository, times(1)).findBySandboxId(eq(sandboxId), any())
    }

    @Test
    @DisplayName("프로젝트 목록 조회 - 빈 목록 반환")
    fun `should return empty list when no projects exist`() {
        // Given
        val sandboxId = 1L
        val pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending())
        val page = PageImpl<Project>(emptyList(), pageable, 0)

        whenever(projectRepository.findBySandboxId(eq(sandboxId), any()))
            .thenReturn(page)

        // When
        val result = projectService.getProjectList(sandboxId, 10, 0, "createdAt", SortDirection.DESCENDING)

        // Then
        assertNotNull(result)
        assertEquals(0, result.totalElements)
        assertTrue(result.content.isEmpty())

        verify(projectRepository, times(1)).findBySandboxId(eq(sandboxId), any())
    }

    // ============ 프로젝트 생성 테스트 ============

    @Test
    @DisplayName("프로젝트 생성 성공")
    fun `should create project successfully`() {
        // Given
        val sandboxId = 1L
        val request = ProjectCreateRequest(
            title = "New Project",
            description = "New Description"
        )

        whenever(sandboxService.getSandboxById(sandboxId))
            .thenReturn(testSandbox)
        whenever(projectRepository.save(any<Project>()))
            .thenAnswer { it.arguments[0] }

        // When
        projectService.createProject(sandboxId, request, testUser.id!!)

        // Then
        verify(sandboxService, times(1)).getSandboxById(sandboxId)
        verify(projectRepository, times(1)).save(any<Project>())
    }

    @Test
    @DisplayName("프로젝트 생성 실패 - 존재하지 않는 샌드박스")
    fun `should fail to create project when sandbox not found`() {
        // Given
        val sandboxId = 999L
        val request = ProjectCreateRequest(
            title = "New Project",
            description = "New Description"
        )

        whenever(sandboxService.getSandboxById(sandboxId))
            .thenThrow(BusinessException.SandboxNotFound(sandboxId))

        // When & Then
        assertThrows(BusinessException.SandboxNotFound::class.java) {
            projectService.createProject(sandboxId, request, testUser.id!!)
        }

        verify(sandboxService, times(1)).getSandboxById(sandboxId)
        verify(projectRepository, never()).save(any())
    }

    // ============ 개별 프로젝트 조회 테스트 ============

    @Test
    @DisplayName("개별 프로젝트 조회 성공")
    fun `should get project successfully`() {
        // Given
        val projectId = 1L
        val sandboxId = 1L

        whenever(projectRepository.findById(projectId))
            .thenReturn(Optional.of(testProject))

        // When
        val result = projectService.getProject(projectId, sandboxId)

        // Then
        assertNotNull(result)
        assertEquals(1L, result.id)
        assertEquals("Test Project", result.title)
        assertEquals(sandboxId, result.sandboxId)

        verify(projectRepository, times(1)).findById(projectId)
    }

    @Test
    @DisplayName("개별 프로젝트 조회 실패 - 존재하지 않는 프로젝트")
    fun `should fail to get project when project not found`() {
        // Given
        val projectId = 999L
        val sandboxId = 1L

        whenever(projectRepository.findById(projectId))
            .thenReturn(Optional.empty())

        // When & Then
        assertThrows(BusinessException.ProjectNotFound::class.java) {
            projectService.getProject(projectId, sandboxId)
        }

        verify(projectRepository, times(1)).findById(projectId)
    }

    @Test
    @DisplayName("개별 프로젝트 조회 실패 - 잘못된 샌드박스 ID")
    fun `should fail to get project when sandbox ID mismatch`() {
        // Given
        val projectId = 1L
        val wrongSandboxId = 999L

        whenever(projectRepository.findById(projectId))
            .thenReturn(Optional.of(testProject))

        // When & Then
        assertThrows(BusinessException.ProjectNotFound::class.java) {
            projectService.getProject(projectId, wrongSandboxId)
        }

        verify(projectRepository, times(1)).findById(projectId)
    }

    // ============ ID로 프로젝트 조회 테스트 ============

    @Test
    @DisplayName("ID로 프로젝트 조회 성공")
    fun `should get project by ID successfully`() {
        // Given
        val projectId = 1L

        whenever(projectRepository.findById(projectId))
            .thenReturn(Optional.of(testProject))

        // When
        val result = projectService.getProjectById(projectId)

        // Then
        assertNotNull(result)
        assertEquals(1L, result.id)
        assertEquals("Test Project", result.title)

        verify(projectRepository, times(1)).findById(projectId)
    }

    @Test
    @DisplayName("ID로 프로젝트 조회 실패 - 존재하지 않는 프로젝트")
    fun `should fail to get project by ID when not found`() {
        // Given
        val projectId = 999L

        whenever(projectRepository.findById(projectId))
            .thenReturn(Optional.empty())

        // When & Then
        assertThrows(BusinessException.ProjectNotFound::class.java) {
            projectService.getProjectById(projectId)
        }

        verify(projectRepository, times(1)).findById(projectId)
    }
}