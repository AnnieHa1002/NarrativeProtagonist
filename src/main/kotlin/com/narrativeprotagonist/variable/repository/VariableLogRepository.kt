package com.narrativeprotagonist.variable.repository

import com.narrativeprotagonist.variable.domain.VariableLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VariableLogRepository : JpaRepository<VariableLog, String> {
    // TODO: 추가 쿼리 메서드 작성
}
