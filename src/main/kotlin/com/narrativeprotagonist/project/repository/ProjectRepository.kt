package com.narrativeprotagonist.project.repository

import com.narrativeprotagonist.project.domain.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository : JpaRepository<Project, String> {
    // TODO: 추가 쿼리 메서드 작성
}
