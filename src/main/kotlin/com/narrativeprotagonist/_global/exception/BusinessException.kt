package com.narrativeprotagonist._global.exception

import org.springframework.http.HttpStatus

/**
 * 비즈니스 로직 예외
 * Sealed Class로 타입 세이프하게 관리
 */
sealed class BusinessException(
    val code: String,
    val status: HttpStatus,
    override val message: String,
    val messageKey: String? = null  // i18n 메시지 키
) : RuntimeException(message) {

    // ============ User 도메인 ============
    data class UserNotFound(val userId: String) : BusinessException(
        code = "USER_001",
        status = HttpStatus.NOT_FOUND,
        message = "User not found: $userId",
        messageKey = "error.user.notFound"
    )

    data class DuplicateEmail(val email: String) : BusinessException(
        code = "USER_002",
        status = HttpStatus.CONFLICT,
        message = "Email already exists: $email",
        messageKey = "error.user.duplicateEmail"
    )

    // ============ Auth 도메인 ============
    data class InvalidToken(val reason: String = "") : BusinessException(
        code = "AUTH_001",
        status = HttpStatus.UNAUTHORIZED,
        message = "Invalid token: $reason",
        messageKey = "error.auth.invalidToken"
    )

    data object TokenExpired : BusinessException(
        code = "AUTH_002",
        status = HttpStatus.UNAUTHORIZED,
        message = "Token has expired",
        messageKey = "error.auth.tokenExpired"
    )

    data class InvalidCredentials(val email: String) : BusinessException(
        code = "AUTH_003",
        status = HttpStatus.UNAUTHORIZED,
        message = "Invalid credentials for: $email",
        messageKey = "error.auth.invalidCredentials"
    )

    data class VerificationExpired(val email: String) : BusinessException(
        code = "AUTH_004",
        status = HttpStatus.BAD_REQUEST,
        message = "Verification link has expired for: $email",
        messageKey = "error.auth.verificationExpired"
    )

    data class UserNotVerified(val email: String) : BusinessException(
        code = "AUTH_005",
        status = HttpStatus.FORBIDDEN,
        message = "User not verified: $email",
        messageKey = "error.auth.userNotVerified"
    )

    // ============ Project 도메인 ============
    data class ProjectNotFound(val projectId: String) : BusinessException(
        code = "PROJECT_001",
        status = HttpStatus.NOT_FOUND,
        message = "Project not found: $projectId",
        messageKey = "error.project.notFound"
    )

    data class UnauthorizedAccess(val resourceType: String, val resourceId: String) : BusinessException(
        code = "PROJECT_002",
        status = HttpStatus.FORBIDDEN,
        message = "Unauthorized access to $resourceType: $resourceId",
        messageKey = "error.project.unauthorized"
    )

    // ============ Sandbox 도메인 ============
    data class SandboxNotFound(val sandboxId: String) : BusinessException(
        code = "SANDBOX_001",
        status = HttpStatus.NOT_FOUND,
        message = "Sandbox not found: $sandboxId",
        messageKey = "error.sandbox.notFound"
    )

    // ============ Common ============
    data class InvalidArgument(val field: String, val reason: String) : BusinessException(
        code = "COMMON_001",
        status = HttpStatus.BAD_REQUEST,
        message = "Invalid argument '$field': $reason",
        messageKey = "error.common.invalidArgument"
    )

    data class ResourceNotFound(val resourceType: String, val resourceId: String) : BusinessException(
        code = "COMMON_002",
        status = HttpStatus.NOT_FOUND,
        message = "$resourceType not found: $resourceId",
        messageKey = "error.common.resourceNotFound"
    )
}
