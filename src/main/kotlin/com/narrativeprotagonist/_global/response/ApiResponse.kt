package com.narrativeprotagonist._global.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null,
    val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        // 성공 응답
        fun <T> success(data: T, message: String? = null): ApiResponse<T> =
            ApiResponse(
                success = true,
                message = message,
                data = data
            )

        // 성공 응답 (데이터 없음)
        fun success(message: String? = "Success"): ApiResponse<Unit> =
            ApiResponse(
                success = true,
                message = message,
                data = null
            )

        // 실패 응답
        fun <T> error(message: String, data: T? = null): ApiResponse<T> =
            ApiResponse(
                success = false,
                message = message,
                data = data
            )
    }
}
