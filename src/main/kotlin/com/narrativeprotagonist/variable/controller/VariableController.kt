package com.narrativeprotagonist.variable.controller

import com.narrativeprotagonist.variable.service.VariableService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/variables")
class VariableController(
    private val variableService: VariableService
) {
    // TODO: API 엔드포인트 작성
}
