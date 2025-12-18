package com.narrativeprotagonist.project.service

import com.narrativeprotagonist._global.exception.BusinessException
import com.narrativeprotagonist.project.domain.Project
import com.narrativeprotagonist.project.dto.ProjectCreateRequest
import com.narrativeprotagonist.project.dto.ProjectResponse
import com.narrativeprotagonist.project.repository.ProjectReleaseRepository
import com.narrativeprotagonist.project.repository.ProjectRepository
import com.narrativeprotagonist.sandbox.service.SandboxService
import com.narrativeprotagonist.user.domain.User
import org.hibernate.query.SortDirection
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val projectReleaseRepository: ProjectReleaseRepository,
    private val sandboxService: SandboxService
) {
    fun getProjectList(sandboxId: String, size: Int, page: Int, sortBy: String, sortDirection: SortDirection):
            Page<ProjectResponse> {
        //pagination and sorting logic can be added here
        val sort = when {
            sortDirection == SortDirection.ASCENDING -> Sort.by(sortBy).ascending()
            else -> Sort.by(sortBy).descending()
        }

        val pageable = PageRequest.of(page.coerceAtLeast(0), size.coerceAtLeast(1), sort)
        val resultPage = projectRepository.findBySandboxId(sandboxId, pageable)
        return resultPage.map { project ->
            ProjectResponse(project, sandboxId)
        }
    }

    fun createProject(sandboxId: String, requestBody: ProjectCreateRequest,
                      user : User
    ) {
        val userId = user.id!!
        val sandbox = sandboxService.getSandboxById(sandboxId)
        val newProject = Project(
            title = requestBody.title,
            description = requestBody.description,
            sandbox = sandbox,
            userId = userId
        )
        projectRepository.save(newProject)
    }

    fun getProject(projectId: String, sandboxId: String): ProjectResponse {
        val project = projectRepository.findById(projectId).orElseThrow {
            BusinessException.ProjectNotFound(projectId)
        }
        if (project.sandbox!!.id != sandboxId) {
            throw BusinessException.ProjectNotFound(projectId)
        }
        return ProjectResponse(project, sandboxId)
    }

    fun getProjectById(projectId: String): Project {
        return projectRepository.findById(projectId).orElseThrow {
            BusinessException.ProjectNotFound(projectId)
        }
    }
}
