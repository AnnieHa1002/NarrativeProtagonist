package com.narrativeprotagonist.node.service

import com.narrativeprotagonist._global.exception.BusinessException
import com.narrativeprotagonist.node.domain.Node
import com.narrativeprotagonist.node.dto.NodeRequest
import com.narrativeprotagonist.node.dto.NodeResponse
import com.narrativeprotagonist.node.repository.NodeRepository
import com.narrativeprotagonist.project.service.ProjectService
import com.narrativeprotagonist.user.domain.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class NodeService(
    private val nodeRepository: NodeRepository,
    private val projectService: ProjectService
) {
    fun getNodes(projectId: String): List<NodeResponse> {
        val nodeList = nodeRepository.findAllByProjectId(projectId)
        return nodeList.map { node ->
            NodeResponse(
                node, projectId
            )
        }
    }

    fun createNode(
        projectId: String,
        nodeRequest: NodeRequest,
        user: User
    ): NodeResponse {
        val project = projectService.getProjectById(projectId)
        val newNode = Node(
            project = project,
            title = nodeRequest.title,
            content = nodeRequest.content,
            authorId = user.id!!,
            nodeType = nodeRequest.nodeType,
            nextNodeId = nodeRequest.nextNodeId,
            conditions = nodeRequest.conditions,
            effects = nodeRequest.effects,
            xOffset = nodeRequest.xOffset,
            yOffset = nodeRequest.yOffset,
        )
        val savedNode = nodeRepository.save(newNode)
        return NodeResponse(savedNode, projectId)
    }

    fun updateNode(
        projectId: String,
        nodeId: String,
        nodeRequest: NodeRequest,
        user: User
    ): NodeResponse {
        val existingNode = nodeRepository.findById(nodeId).orElseThrow {
            BusinessException.NodeNotFound(nodeId)
        }

        if (existingNode.authorId != user.id) {
            throw BusinessException.UnauthorizedAction("You are not authorized to update this node.")
        }
        existingNode.update(
            title = nodeRequest.title,
            content = nodeRequest.content,
            nodeType = nodeRequest.nodeType,
            nextNodeId = nodeRequest.nextNodeId,
            conditions = nodeRequest.conditions,
            effects = nodeRequest.effects,
            xOffset = nodeRequest.xOffset,
            yOffset = nodeRequest.yOffset,

            )
        nodeRepository.save(existingNode)
        return NodeResponse(existingNode, projectId)
    }
}
