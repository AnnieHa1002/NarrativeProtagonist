package com.narrativeprotagonist.auth.dto

import com.narrativeprotagonist._global.enums.SignInStatusType


data class SignUpRequest(
    val email: String,
    val nickname: String,
)

data class SignUpResponse(
    val id: String?,
    val email: String,
    val expiredAt: Long?,
)

data class SignInRequest(
    val email: String,
)

// ============ 로그인 요청 ============
data class SignInRequestResponse(
    val loginTokenId: String,
)

// ============ 로그인 상태 확인 ============
data class SignInStatusResponse(
    val status: SignInStatusType,  // PENDING, SUCCESS
)
data class SignInStatus(
    val status: SignInStatusType,  // PENDING, SUCCESS
    val tokens: JwtTokens?,
)

// ============ 이메일 링크 검증 ============
data class VerifyLoginResponse(
    val success: Boolean,
)

// ============ JWT 토큰 ============
data class JwtTokens(
    val accessToken: String,
    val refreshToken: String,
)