package com.narrativeprotagonist.trigger.controller

import com.narrativeprotagonist.trigger.service.TriggerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/triggers")
class TriggerController(
    private val triggerService: TriggerService
) {
    // TODO: API 엔드포인트 작성
}
