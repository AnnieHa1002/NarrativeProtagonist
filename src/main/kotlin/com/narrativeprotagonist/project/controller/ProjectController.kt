package com.narrativeprotagonist.project.controller

import com.narrativeprotagonist.project.service.ProjectService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/projects")
class ProjectController(
    private val projectService: ProjectService
) {
    // TODO: API 엔드포인트 작성
}
