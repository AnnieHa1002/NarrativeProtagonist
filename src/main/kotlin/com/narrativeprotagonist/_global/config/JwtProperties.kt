package com.narrativeprotagonist._global.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secret: String = "",
    val expiration: Long = 86400000 // 24시간
)
