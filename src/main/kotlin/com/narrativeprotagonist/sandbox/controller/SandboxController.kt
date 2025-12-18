package com.narrativeprotagonist.sandbox.controller

import com.narrativeprotagonist._global.response.ApiResponse
import com.narrativeprotagonist.auth.dto.SignUpRequest
import com.narrativeprotagonist.auth.dto.SignUpResponse
import com.narrativeprotagonist.sandbox.dto.SandboxResponse
import com.narrativeprotagonist.sandbox.service.SandboxService
import com.narrativeprotagonist.user.domain.User
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sandboxes")
@PreAuthorize("isAuthenticated()")
class SandboxController(
    private val sandboxService: SandboxService
) {
    @GetMapping("")
    fun getUserSandbox(@AuthenticationPrincipal user: User ): ApiResponse<SandboxResponse> {
        val response = sandboxService.getUserSandbox(user.id!!)
        return ApiResponse.success(response)
    }

    @GetMapping("/lists")
    fun getSandboxList(@AuthenticationPrincipal user: User ): ApiResponse<List<SandboxResponse>> {
        val response = sandboxService.getSandboxList(user)
        return ApiResponse.success(response)
    }

}
