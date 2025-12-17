package com.narrativeprotagonist._global.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(CookieProperties::class)
class CookieConfig