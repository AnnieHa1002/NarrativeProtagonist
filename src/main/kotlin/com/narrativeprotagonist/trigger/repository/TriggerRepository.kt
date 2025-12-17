package com.narrativeprotagonist.trigger.repository

import com.narrativeprotagonist.trigger.domain.Trigger
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TriggerRepository : JpaRepository<Trigger, String> {
    // TODO: 추가 쿼리 메서드 작성
}
