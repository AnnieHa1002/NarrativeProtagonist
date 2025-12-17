package com.narrativeprotagonist._global.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val accessSecret: String,
    val refreshSecret: String,
    val expiration: Long,
    val refreshExpiration: Long
)
