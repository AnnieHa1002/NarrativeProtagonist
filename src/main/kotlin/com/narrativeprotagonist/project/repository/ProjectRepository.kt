package com.narrativeprotagonist.project.repository

import com.narrativeprotagonist.project.domain.Project
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository : JpaRepository<Project, Long> {
    fun findAllBySandboxId(sandboxId: Long): List<Project>
    fun findBySandboxId(sandboxId: Long, pageable: Pageable): Page<Project>
}
