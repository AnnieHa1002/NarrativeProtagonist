package com.narrativeprotagonist.auth.controller

import com.narrativeprotagonist._global.config.CookieProperties
import com.narrativeprotagonist._global.constants.AppConstants
import com.narrativeprotagonist._global.enums.SignInStatusType
import com.narrativeprotagonist._global.response.ApiResponse
import com.narrativeprotagonist.auth.dto.*
import com.narrativeprotagonist.auth.service.AuthService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseCookie
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

    // ============ 매직 링크 로그인 ============
    /**
     * Step 1: 로그인 요청 (이메일 입력)
     */
    @PostMapping("/request-login")
    fun requestLogin(@RequestBody request: SignInRequest): ApiResponse<SignInRequestResponse> {
        return ApiResponse.success(authService.requestLogin(request), "이메일을 확인해주세요")
    }

    /**
     * Step 2: 로그인 상태 확인 (Polling용)
     * 프론트엔드에서 2초마다 호출 필요
     */
    @GetMapping("/login-status/{loginTokenId}")
    fun checkLoginStatus(
        @PathVariable loginTokenId: String,
        response: HttpServletResponse
    ): ApiResponse<SignInStatusResponse> {
        val status = authService.checkLoginStatus(loginTokenId)

        // JWT가 생성되었으면 HttpOnly Cookie로 설정
        if (status.status == SignInStatusType.SUCCESS && status.tokens != null) {
            // Access Token 쿠키
            val accessCookie = ResponseCookie.from(AppConstants.Cookie.ACCESS_TOKEN, status.tokens.accessToken)
                .httpOnly(true)
                .secure(cookieProperties.secure)
                .path(AppConstants.Cookie.PATH)
                .maxAge(AppConstants.Cookie.MAX_AGE_24H.toLong())
                .sameSite("Strict")
                .build()
            response.addHeader("Set-Cookie", accessCookie.toString())

            // Refresh Token 쿠키
            val refreshCookie = ResponseCookie.from(AppConstants.Cookie.REFRESH_TOKEN, status.tokens.refreshToken)
                .httpOnly(true)
                .secure(cookieProperties.secure)
                .path(AppConstants.Cookie.PATH)
                .maxAge(AppConstants.Cookie.MAX_AGE_7D.toLong())
                .sameSite("Strict")
                .build()
            response.addHeader("Set-Cookie", refreshCookie.toString())
        }

        return ApiResponse.success(
            SignInStatusResponse(
                status = status.status
            )
        )
    }

    /**
     * Step 3: 이메일 링크 클릭 시 호출
     */
    @GetMapping("/verify-login")
    fun verifyLoginToken(@RequestParam token: String): ApiResponse<Unit> {
        authService.verifyLoginToken(token)
        return ApiResponse.success("verified")
    }

    @PostMapping("/reissue")
    fun reissue(
        @CookieValue(AppConstants.Cookie.REFRESH_TOKEN) refreshToken: String,
        response: HttpServletResponse
    ): ApiResponse<Unit> {
        val tokens = authService.reissueTokensRequest(refreshToken)

        // Access Token 쿠키
        val accessCookie = ResponseCookie.from(AppConstants.Cookie.ACCESS_TOKEN, tokens.accessToken)
            .httpOnly(true)
            .secure(cookieProperties.secure)
            .path(AppConstants.Cookie.PATH)
            .maxAge(AppConstants.Cookie.MAX_AGE_24H.toLong())
            .sameSite("Strict")
            .build()
        response.addHeader("Set-Cookie", accessCookie.toString())

        // Refresh Token 쿠키
        val refreshCookie = ResponseCookie.from(AppConstants.Cookie.REFRESH_TOKEN, tokens.refreshToken)
            .httpOnly(true)
            .secure(cookieProperties.secure)
            .path(AppConstants.Cookie.PATH)
            .maxAge(AppConstants.Cookie.MAX_AGE_7D.toLong())
            .sameSite("Strict")
            .build()
        response.addHeader("Set-Cookie", refreshCookie.toString())

        return ApiResponse.success("Tokens reissued successfully")
    }
}


