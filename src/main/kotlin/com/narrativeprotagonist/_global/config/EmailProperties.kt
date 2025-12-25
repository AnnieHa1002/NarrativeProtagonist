package com.narrativeprotagonist._global.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.email")
data class EmailProperties(
    val from: String ,
    val baseUrl: String
)
