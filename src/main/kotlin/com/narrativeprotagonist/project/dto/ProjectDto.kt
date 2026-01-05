package com.narrativeprotagonist.project.dto

import com.narrativeprotagonist.project.domain.Project


data class ProjectResponse(
    val id: Long,
    val title: String,
    val description: String,
    val sandboxId: Long,
){
    constructor(project : Project, sandboxId: Long) : this(
        id = project.id,
        title = project.title,
        description = project.description,
        sandboxId = sandboxId
    )
}

data class ProjectCreateRequest (
    val title: String,
    val description: String
)