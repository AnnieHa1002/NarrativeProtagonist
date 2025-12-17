package com.narrativeprotagonist.choice.repository

import com.narrativeprotagonist.choice.domain.Choice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChoiceRepository : JpaRepository<Choice, String> {
    // TODO: 추가 쿼리 메서드 작성
}
