package com.narrativeprotagonist.project.controller

import com.narrativeprotagonist._global.response.ApiResponse
import com.narrativeprotagonist.project.dto.*
import com.narrativeprotagonist.project.service.ProjectService
import com.narrativeprotagonist.user.domain.User
import org.hibernate.query.SortDirection
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sandboxes/{sandboxId}/projects")
@PreAuthorize("isAuthenticated()")
class ProjectController(
    private val projectService: ProjectService
) {

    @GetMapping("")
    fun getProjectList(@PathVariable sandboxId: String , @RequestParam size : Int = 10, @RequestParam page : Int
    = 0,
    @RequestParam sortBy : String = "createdAt", @RequestParam sortDirection: SortDirection = SortDirection.DESCENDING):
            ApiResponse<Page<ProjectResponse>> {
        val response = projectService.getProjectList(sandboxId, size, page, sortBy,sortDirection)
        return ApiResponse.success(response)
    }

    @PostMapping("")
    fun createProject(@PathVariable sandboxId: String, @RequestBody requestBody : ProjectCreateRequest,
                      @AuthenticationPrincipal
    user : User): ApiResponse<Any> {
        val response = projectService.createProject(sandboxId, requestBody, user)
        return ApiResponse.success(response)
    }
    @GetMapping("/{projectId}")
    fun getProject(@PathVariable sandboxId: String ,@PathVariable projectId : String): ApiResponse<ProjectResponse> {
        val response = projectService.getProject(projectId, sandboxId)
        return ApiResponse.success(response)
    }

}
