package com.narrativeprotagonist.node.service

import com.narrativeprotagonist.node.repository.NodeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class NodeService(
    private val nodeRepository: NodeRepository
) {
    // TODO: 비즈니스 로직 작성
}
