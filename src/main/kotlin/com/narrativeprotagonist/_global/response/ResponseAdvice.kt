package com.narrativeprotagonist._global.response

import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice(basePackages = ["com.narrativeprotagonist"])
class ResponseAdvice : ResponseBodyAdvice<Any> {

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean {
        // ApiResponse로 이미 래핑된 경우는 제외
        return returnType.parameterType != ApiResponse::class.java
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        // Unit 타입 (void) 처리
        if (body == null || returnType.parameterType == Unit::class.java) {
            return ApiResponse.success("Success")
        }

        // 이미 ApiResponse인 경우 그대로 반환
        if (body is ApiResponse<*>) {
            return body
        }

        // 일반 응답을 ApiResponse로 래핑
        return ApiResponse.success(body)
    }
}
