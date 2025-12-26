package com.narrativeprotagonist.effect.repository

import com.narrativeprotagonist.effect.domain.Effect
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EffectRepository : JpaRepository<Effect, String> {

    fun findAllByNodeId(nodeId: String): List<Effect>
    // TODO: 추가 쿼리 메서드 작성
}
