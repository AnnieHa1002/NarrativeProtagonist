package com.narrativeprotagonist.auth.controller

import com.narrativeprotagonist._global.config.CookieProperties
import com.narrativeprotagonist._global.enums.SignInStatusType
import com.narrativeprotagonist._global.constants.AppConstants
import com.narrativeprotagonist._global.response.ApiResponse
import com.narrativeprotagonist.auth.service.AuthService
import com.narrativeprotagonist.auth.dto.SignInRequest
import com.narrativeprotagonist.auth.dto.SignInRequestResponse
import com.narrativeprotagonist.auth.dto.SignInStatusResponse
import com.narrativeprotagonist.auth.dto.SignUpRequest
import com.narrativeprotagonist.auth.dto.SignUpResponse
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val cookieProperties: CookieProperties
) {

    // ============ 회원가입 ============
    @PostMapping("/sign-up")
    fun signUp(@RequestBody signUpRequest: SignUpRequest): ApiResponse<SignUpResponse> {
        return ApiResponse.success(data = authService.signUp(signUpRequest))
    }

    @GetMapping("/verify")
    fun verifyEmail(
        @RequestParam("email") email: String,
        @RequestParam("expiredAt") expiredAt: Long
    ): ApiResponse<Boolean> {
        return ApiResponse.success(data = authService.verifyEmail(email, expiredAt))
    }

}


