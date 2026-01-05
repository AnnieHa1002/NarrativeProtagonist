package com.narrativeprotagonist.node.controller

import com.narrativeprotagonist._global.response.ApiResponse
import com.narrativeprotagonist.node.dto.NodeRequest
import com.narrativeprotagonist.node.dto.NodeResponse
import com.narrativeprotagonist.node.service.NodeService
import com.narrativeprotagonist.user.domain.User
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController

@RequestMapping("/api/projects/{projectId}/nodes")
@PreAuthorize("isAuthenticated()")
class NodeController(
    private val nodeService: NodeService
) {

    @GetMapping("")
    fun getNodes(
        @PathVariable projectId: Long
    ): ApiResponse<List<NodeResponse>> {
        val response = nodeService.getNodes(projectId)
        return ApiResponse.success(response)
    }

    @PostMapping("")
    fun createNode(
        @PathVariable projectId: Long, @RequestBody nodeRequest: NodeRequest, @AuthenticationPrincipal userId: Long
    ): ApiResponse<NodeResponse> {
        val response = nodeService.createNode(projectId, nodeRequest, userId)
        return ApiResponse.success(response)
    }

    @PostMapping("/{nodeId}")
    fun updateNode(
        @PathVariable projectId: Long,
        @PathVariable nodeId: Long, @RequestBody nodeRequest: NodeRequest, @AuthenticationPrincipal userId: Long
    ): ApiResponse<NodeResponse> {
        val response = nodeService.updateNode(projectId, nodeId,nodeRequest, userId)
        return ApiResponse.success(response)
    }
}
