package com.narrativeprotagonist.condition.repository

import com.narrativeprotagonist.condition.domain.Condition
import com.narrativeprotagonist.node.domain.Node
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ConditionRepository : JpaRepository<Condition, Long> {

    fun findAllByNodeId(nodeId: Long): List<Condition>
    // TODO: 추가 쿼리 메서드 작성
    // ⭐ 추가 필요 - Choice 조회
    fun findAllByChoiceId(choiceId: Long): List<Condition>

    // ⭐ 추가 권장 - Fetch Join으로 N+1 방지
    @Query("""
        SELECT c FROM Condition c
        LEFT JOIN FETCH c.variable
        LEFT JOIN FETCH c.item
        WHERE c.node.id = :nodeId
    """)
    fun findAllByNodeIdWithDetails(nodeId: Long): List<Condition>

    @Query("""
        SELECT c FROM Condition c
        LEFT JOIN FETCH c.variable
        LEFT JOIN FETCH c.item
        WHERE c.choice.id = :choiceId
    """)
    fun findAllByChoiceIdWithDetails(choiceId: Long): List<Condition>
}
