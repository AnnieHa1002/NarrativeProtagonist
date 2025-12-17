package com.narrativeprotagonist.user.dto

import com.narrativeprotagonist.user.domain.User

// DTO classes
data class UserResponse(
    val id: String?,
    val email: String,
    val nickname: String?,
    val verified: Boolean,
    val createdAt: Long?,
    val modifiedAt: Long?
)

fun User.toResponse() = UserResponse(
    id = id,
    email = email,
    nickname = nickname,
    verified = verified,
    createdAt = createdAtEpochMilli(),
    modifiedAt = modifiedAtEpochMilli()
)
