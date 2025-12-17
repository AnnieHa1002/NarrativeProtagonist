package com.narrativeprotagonist.sandbox.controller

import com.narrativeprotagonist.sandbox.service.SandboxService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sandboxes")
class SandboxController(
    private val sandboxService: SandboxService
) {
    // TODO: API 엔드포인트 작성
}
