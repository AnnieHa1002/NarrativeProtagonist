package com.narrativeprotagonist.node.repository

import com.narrativeprotagonist.node.domain.Node
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NodeRepository : JpaRepository<Node, String> {
    // TODO: 추가 쿼리 메서드 작성
}
