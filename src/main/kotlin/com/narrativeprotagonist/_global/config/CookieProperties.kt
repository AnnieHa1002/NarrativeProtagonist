package com.narrativeprotagonist._global.config

import jakarta.servlet.http.Cookie
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "app.cookie")
class CookieProperties
@ConstructorBinding constructor(
    val secure: Boolean
) {
    fun apply(cookie: Cookie) {
        cookie.isHttpOnly = true
        cookie.secure = secure
    }
}
