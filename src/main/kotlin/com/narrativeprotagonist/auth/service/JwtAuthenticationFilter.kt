package com.narrativeprotagonist.auth.service

import com.narrativeprotagonist._global.constants.AppConstants
import com.narrativeprotagonist._global.enums.JwtTokenType
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = resolveToken(request)

            // 토큰이 없으면 인증 없이 다음 필터로
            if (token == null) {
                filterChain.doFilter(request, response)
                return
            }

            // 토큰 검증
            if (jwtUtils.validateToken(token, JwtTokenType.ACCESS)) {
                val userId = jwtUtils.getUserIdFromToken(token, JwtTokenType.ACCESS)

                val authentication = UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    emptyList()
                )

                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            logger.error("JWT authentication failed: ${e.message}")
        }

        filterChain.doFilter(request, response)
    }

    /**
     * HttpServletRequest의 쿠키에서 JWT 토큰 추출
     */
    private fun resolveToken(request: HttpServletRequest): String? {
        val cookies = request.cookies ?: return null

        return cookies
            .firstOrNull { it.name == AppConstants.Cookie.ACCESS_TOKEN }
            ?.value
    }
}