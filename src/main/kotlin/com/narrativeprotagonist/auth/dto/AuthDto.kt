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

