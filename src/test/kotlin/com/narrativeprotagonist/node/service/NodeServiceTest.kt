package com.narrativeprotagonist.node.service

import com.narrativeprotagonist._global.enums.NodeType
import com.narrativeprotagonist._global.exception.BusinessException
import com.narrativeprotagonist.node.domain.Node
import com.narrativeprotagonist.node.dto.NodeRequest
import com.narrativeprotagonist.node.repository.NodeRepository
import com.narrativeprotagonist.project.domain.Project
import com.narrativeprotagonist.project.service.ProjectService
import com.narrativeprotagonist.sandbox.domain.Sandbox
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
 * NodeService 단위 테스트
 *
 * Mock을 사용하여 의존성을 격리하고 NodeService의 비즈니스 로직만 테스트합니다.
 */
@ExtendWith(MockitoExtension::class)
class NodeServiceTest {

    @Mock
    private lateinit var nodeRepository: NodeRepository

    @Mock
    private lateinit var projectService: ProjectService

    @InjectMocks
    private lateinit var nodeService: NodeService

    private lateinit var testUser: User
    private lateinit var testSandbox: Sandbox
    private lateinit var testProject: Project
    private lateinit var testNode: Node

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

        testNode = Node(
            project = testProject,
            authorId = testUser.id!!.toString(),
            title = "Test Node",
            content = "Test Content",
            nodeType = NodeType.SCENE,
            nextNodeId = null,
            originalNode = null,
            version = 1,
            xOffset = 0,
            yOffset = 0
        ).apply {
            id = 1L
        }
    }

    // ============ 노드 목록 조회 테스트 ============

    @Test
    @DisplayName("노드 목록 조회 성공")
    fun `should get nodes successfully`() {
        // Given
        val projectId = 1L
        val nodes = listOf(
            testNode,
            Node(
                project = testProject,
                authorId = testUser.id!!.toString(),
                title = "Second Node",
                content = "Second Content",
                nodeType = NodeType.BRANCH,
                nextNodeId = 3L,
                originalNode = null,
                version = 1,
                xOffset = 100,
                yOffset = 100
            ).apply {
                id = 2L
            }
        )

        whenever(nodeRepository.findAllByProjectId(projectId))
            .thenReturn(nodes)

        // When
        val result = nodeService.getNodes(projectId)

        // Then
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals(1L, result[0].id)
        assertEquals("Test Node", result[0].title)
        assertEquals(NodeType.SCENE, result[0].nodeType)
        assertEquals(2L, result[1].id)
        assertEquals(NodeType.BRANCH, result[1].nodeType)

        verify(nodeRepository, times(1)).findAllByProjectId(projectId)
    }

    @Test
    @DisplayName("노드 목록 조회 - 빈 목록 반환")
    fun `should return empty list when project has no nodes`() {
        // Given
        val projectId = 1L

        whenever(nodeRepository.findAllByProjectId(projectId))
            .thenReturn(emptyList())

        // When
        val result = nodeService.getNodes(projectId)

        // Then
        assertNotNull(result)
        assertTrue(result.isEmpty())

        verify(nodeRepository, times(1)).findAllByProjectId(projectId)
    }

    // ============ 노드 생성 테스트 ============

    @Test
    @DisplayName("노드 생성 성공 - SCENE 타입")
    fun `should create scene node successfully`() {
        // Given
        val projectId = 1L
        val request = NodeRequest(
            title = "New Node",
            content = "New Content",
            nodeType = NodeType.SCENE,
            nextNodeId = null,
            xOffset = 100,
            yOffset = 200
        )

        val savedNode = Node(
            project = testProject,
            authorId = testUser.id!!.toString(),
            title = request.title,
            content = request.content,
            nodeType = request.nodeType,
            nextNodeId = request.nextNodeId,
            originalNode = null,
            version = 0,
            xOffset = request.xOffset,
            yOffset = request.yOffset
        ).apply {
            id = 2L
        }

        whenever(projectService.getProjectById(projectId))
            .thenReturn(testProject)
        whenever(nodeRepository.save(any<Node>()))
            .thenReturn(savedNode)

        // When
        val result = nodeService.createNode(projectId, request, testUser.id!!)

        // Then
        assertNotNull(result)
        assertEquals(2L, result.id)
        assertEquals("New Node", result.title)
        assertEquals("New Content", result.content)
        assertEquals(NodeType.SCENE, result.nodeType)
        assertEquals(projectId, result.projectId)

        verify(projectService, times(1)).getProjectById(projectId)
        verify(nodeRepository, times(1)).save(any<Node>())
    }

    @Test
    @DisplayName("노드 생성 성공 - ENTRY 타입")
    fun `should create entry node successfully`() {
        // Given
        val projectId = 1L
        val request = NodeRequest(
            title = "Entry Node",
            content = "Story begins here",
            nodeType = NodeType.ENTRY,
            nextNodeId = 2L,
            xOffset = 0,
            yOffset = 0
        )

        val savedNode = Node(
            project = testProject,
            authorId = testUser.id!!.toString(),
            title = request.title,
            content = request.content,
            nodeType = NodeType.ENTRY,
            nextNodeId = 2L,
            originalNode = null,
            version = 0,
            xOffset = 0,
            yOffset = 0
        ).apply {
            id = 3L
        }

        whenever(projectService.getProjectById(projectId))
            .thenReturn(testProject)
        whenever(nodeRepository.save(any<Node>()))
            .thenReturn(savedNode)

        // When
        val result = nodeService.createNode(projectId, request, testUser.id!!)

        // Then
        assertNotNull(result)
        assertEquals(NodeType.ENTRY, result.nodeType)
        assertEquals(2L, result.nextNodeId)

        verify(projectService, times(1)).getProjectById(projectId)
        verify(nodeRepository, times(1)).save(any<Node>())
    }

    @Test
    @DisplayName("노드 생성 실패 - 존재하지 않는 프로젝트")
    fun `should fail to create node when project not found`() {
        // Given
        val projectId = 999L
        val request = NodeRequest(
            title = "New Node",
            content = "New Content",
            nodeType = NodeType.SCENE,
            nextNodeId = null,
            xOffset = 0,
            yOffset = 0
        )

        whenever(projectService.getProjectById(projectId))
            .thenThrow(BusinessException.ProjectNotFound(projectId))

        // When & Then
        assertThrows(BusinessException.ProjectNotFound::class.java) {
            nodeService.createNode(projectId, request, testUser.id!!)
        }

        verify(projectService, times(1)).getProjectById(projectId)
        verify(nodeRepository, never()).save(any())
    }

    // ============ 노드 수정 테스트 ============

    @Test
    @DisplayName("노드 수정 성공")
    fun `should update node successfully`() {
        // Given
        val projectId = 1L
        val nodeId = 1L
        val request = NodeRequest(
            title = "Updated Node",
            content = "Updated Content",
            nodeType = NodeType.BRANCH,
            nextNodeId = 2L,
            xOffset = 150,
            yOffset = 250
        )

        whenever(nodeRepository.findById(nodeId))
            .thenReturn(Optional.of(testNode))
        whenever(nodeRepository.save(any<Node>()))
            .thenAnswer { it.arguments[0] }

        // When
        val result = nodeService.updateNode(projectId, nodeId, request, testUser.id!!)

        // Then
        assertNotNull(result)
        assertEquals(nodeId, result.id)
        assertEquals("Updated Node", result.title)
        assertEquals("Updated Content", result.content)
        assertEquals(NodeType.BRANCH, result.nodeType)

        verify(nodeRepository, times(1)).findById(nodeId)
        verify(nodeRepository, times(1)).save(any<Node>())
    }

    @Test
    @DisplayName("노드 수정 실패 - 존재하지 않는 노드")
    fun `should fail to update node when node not found`() {
        // Given
        val projectId = 1L
        val nodeId = 999L
        val request = NodeRequest(
            title = "Updated Node",
            content = "Updated Content",
            nodeType = NodeType.SCENE,
            nextNodeId = null,
            xOffset = 0,
            yOffset = 0
        )

        whenever(nodeRepository.findById(nodeId))
            .thenReturn(Optional.empty())

        // When & Then
        assertThrows(BusinessException.NodeNotFound::class.java) {
            nodeService.updateNode(projectId, nodeId, request, testUser.id!!)
        }

        verify(nodeRepository, times(1)).findById(nodeId)
        verify(nodeRepository, never()).save(any())
    }

    @Test
    @DisplayName("노드 수정 실패 - 권한 없음 (다른 사용자)")
    fun `should fail to update node when user is not author`() {
        // Given
        val projectId = 1L
        val nodeId = 1L
        val otherUser = User(
            email = "other@example.com",
            nickname = "OtherUser"
        ).apply {
            id = 2L
        }
        val request = NodeRequest(
            title = "Updated Node",
            content = "Updated Content",
            nodeType = NodeType.SCENE,
            nextNodeId = null,
            xOffset = 0,
            yOffset = 0
        )

        whenever(nodeRepository.findById(nodeId))
            .thenReturn(Optional.of(testNode))

        // When & Then
        assertThrows(BusinessException.UnauthorizedAction::class.java) {
            nodeService.updateNode(projectId, nodeId, request, otherUser.id!!)
        }

        verify(nodeRepository, times(1)).findById(nodeId)
        verify(nodeRepository, never()).save(any())
    }

    @Test
    @DisplayName("노드 수정 성공 - 기본 업데이트")
    fun `should update node successfully with all fields`() {
        // Given
        val projectId = 1L
        val nodeId = 1L
        val request = NodeRequest(
            title = "Node Updated",
            content = "Content Updated",
            nodeType = NodeType.BRANCH,
            nextNodeId = 2L,
            xOffset = 100,
            yOffset = 200
        )

        whenever(nodeRepository.findById(nodeId))
            .thenReturn(Optional.of(testNode))
        whenever(nodeRepository.save(any<Node>()))
            .thenAnswer { it.arguments[0] }

        // When
        val result = nodeService.updateNode(projectId, nodeId, request, testUser.id!!)

        // Then
        assertNotNull(result)
        assertEquals("Node Updated", result.title)
        assertEquals("Content Updated", result.content)
        assertEquals(NodeType.BRANCH, result.nodeType)

        verify(nodeRepository, times(1)).findById(nodeId)
        verify(nodeRepository, times(1)).save(any<Node>())
    }
}