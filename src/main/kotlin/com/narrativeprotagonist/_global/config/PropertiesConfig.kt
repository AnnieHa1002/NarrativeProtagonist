package com.narrativeprotagonist._global.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * 모든 ConfigurationProperties를 한 곳에서 등록 관리
 */
@Configuration
@EnableConfigurationProperties(
    EmailProperties::class,
    // 여기에 새로운 Properties 추가
)
class PropertiesConfig
