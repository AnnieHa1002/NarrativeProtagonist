package com.narrativeprotagonist._global.exception

import com.narrativeprotagonist._global.response.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler(
    private val messageSource: MessageSource
) {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    /**
     * BusinessException 처리 (Sealed Class)
     */
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ApiResponse<ErrorDetail>> {
        logger.warn("BusinessException: [{}] {}", e.code, e.message)

        val locale = LocaleContextHolder.getLocale()
        val localizedMessage = e.messageKey?.let {
            messageSource.getMessage(it, null, e.message, locale)
        } ?: e.message

        val errorDetail = ErrorDetail(
            code = e.code,
            message = localizedMessage,
            details = getExceptionDetails(e)
        )

        return ResponseEntity
            .status(e.status)
            .body(ApiResponse.error(localizedMessage, errorDetail))
    }

    /**
     * 예외별 추가 정보 추출
     */
    private fun getExceptionDetails(e: BusinessException): Map<String, Any?> = when (e) {
        is BusinessException.UserNotFound -> mapOf("userId" to e.userId)
        is BusinessException.DuplicateEmail -> mapOf("email" to e.email)
        is BusinessException.ProjectNotFound -> mapOf("projectId" to e.projectId)
        is BusinessException.UnauthorizedAccess -> mapOf(
            "resourceType" to e.resourceType,
            "resourceId" to e.resourceId
        )
        is BusinessException.InvalidArgument -> mapOf(
            "field" to e.field,
            "reason" to e.reason
        )
        else -> emptyMap()
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(e: NoSuchElementException): ResponseEntity<ApiResponse<Unit>> {
        logger.warn("NoSuchElementException: {}", e.message)
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(e.message ?: "Resource not found"))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ApiResponse<Unit>> {
        logger.warn("IllegalArgumentException: {}", e.message)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(e.message ?: "Invalid argument"))
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(e: IllegalStateException): ResponseEntity<ApiResponse<Unit>> {
        logger.warn("IllegalStateException: {}", e.message)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(e.message ?: "Invalid state"))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        logger.error("Unexpected exception", e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error("Internal server error"))
    }
}

/**
 * 에러 상세 정보
 */
data class ErrorDetail(
    val code: String,
    val message: String,
    val details: Map<String, Any?> = emptyMap()
)

