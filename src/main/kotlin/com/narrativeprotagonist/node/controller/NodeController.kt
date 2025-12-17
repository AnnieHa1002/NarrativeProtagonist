package com.narrativeprotagonist.node.controller

import com.narrativeprotagonist.node.service.NodeService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/nodes")
class NodeController(
    private val nodeService: NodeService
) {
    // TODO: API 엔드포인트 작성
}
