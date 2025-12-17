package com.narrativeprotagonist.project.service

import com.narrativeprotagonist.project.repository.ProjectRepository
import com.narrativeprotagonist.project.repository.ProjectReleaseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val projectReleaseRepository: ProjectReleaseRepository
) {
    // TODO: 비즈니스 로직 작성
}
