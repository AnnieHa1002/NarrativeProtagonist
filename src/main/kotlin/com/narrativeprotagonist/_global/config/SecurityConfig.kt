package com.narrativeprotagonist._global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()
                it.requestMatchers("/api/auth/**").permitAll()  // 인증 API는 모두 허용
                it.requestMatchers("/api/public/**").permitAll()  // public API는 모두 허용
                  .anyRequest().authenticated()  // 나머지는 인증 필요
            }

        return http.build()
    }

    /**
     * CORS 설정 (프론트엔드와 통신)
     */
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        // 허용할 Origin (프론트엔드 URL)
        configuration.allowedOrigins = listOf(
            "http://localhost:3000",  // React 개발 서버
            "http://localhost:5173",  // Vite 개발 서버
             // 프로덕션 도메인 있을 시 추가. 예: "https://www.yourdomain.com"
        )

        // 허용할 HTTP 메서드
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")

        // 허용할 헤더
        configuration.allowedHeaders = listOf("*")

        // 쿠키 전송 허용
        configuration.allowCredentials = true

        // 노출할 헤더
        configuration.exposedHeaders = listOf("Authorization")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
