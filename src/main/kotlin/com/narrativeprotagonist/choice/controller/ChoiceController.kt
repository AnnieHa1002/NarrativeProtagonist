package com.narrativeprotagonist.choice.controller

import com.narrativeprotagonist._global.response.ApiResponse
import com.narrativeprotagonist.choice.dto.ChoiceRequest
import com.narrativeprotagonist.choice.service.ChoiceService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/choices")
class ChoiceController(
    private val choiceService: ChoiceService
) {

    @PostMapping("")
    fun createChoice(@AuthenticationPrincipal userId: Long, @RequestBody requestBody: ChoiceRequest):
            ApiResponse<Unit> {
        val response = choiceService.createChoices(userId, requestBody)
        return ApiResponse.success(response)
    }
    // TODO: API 엔드포인트 작성
}
