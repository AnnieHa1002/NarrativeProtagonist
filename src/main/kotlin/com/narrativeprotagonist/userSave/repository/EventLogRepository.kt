package com.narrativeprotagonist.userSave.repository

import com.narrativeprotagonist.userSave.domain.EventLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventLogRepository : JpaRepository<EventLog, String> {
    // TODO: 추가 쿼리 메서드 작성
}
