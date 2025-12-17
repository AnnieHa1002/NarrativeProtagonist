package com.narrativeprotagonist.choice.controller

import com.narrativeprotagonist.choice.service.ChoiceService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/choices")
class ChoiceController(
    private val choiceService: ChoiceService
) {
    // TODO: API 엔드포인트 작성
}
