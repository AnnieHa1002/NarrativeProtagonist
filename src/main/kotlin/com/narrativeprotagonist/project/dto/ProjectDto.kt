package com.narrativeprotagonist.project.dto

import com.narrativeprotagonist.project.domain.Project


data class ProjectResponse(
    val id: String,
    val title: String,
    val description: String,
    val sandboxId: String,
){
    constructor(project : Project, sandboxId: String) : this(
        id = project.id!!,
        title = project.title,
        description = project.description,
        sandboxId = sandboxId
    )
}

data class ProjectCreateRequest (
    val title: String,
    val description: String
)