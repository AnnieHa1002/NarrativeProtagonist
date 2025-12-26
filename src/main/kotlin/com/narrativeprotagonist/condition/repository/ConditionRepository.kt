package com.narrativeprotagonist.condition.repository

import com.narrativeprotagonist.condition.domain.Condition
import com.narrativeprotagonist.node.domain.Node
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ConditionRepository : JpaRepository<Condition, String> {

    fun findAllByNodeId(nodeId: String): List<Condition>
    // TODO: 추가 쿼리 메서드 작성
}
