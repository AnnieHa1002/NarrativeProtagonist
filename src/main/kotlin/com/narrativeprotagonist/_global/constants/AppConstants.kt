package com.narrativeprotagonist._global.constants

object AppConstants {

    /**
     * Cookie 관련 상수
     */
    object Cookie {
        const val ACCESS_TOKEN = "accessToken"
        const val REFRESH_TOKEN = "refreshToken"
        const val MAX_AGE_24H = 24 * 60 * 60  // 24시간
        const val MAX_AGE_7D = 7 * 24 * 60 * 60  // 7일
        const val PATH = "/"
    }

    /**
     * JWT 관련 상수
     */
    object Jwt {
        const val CLAIM_EMAIL = "email"
        const val CLAIM_NICKNAME = "nickname"
        const val CLAIM_USER_ID = "userId"
    }

    /**
     * 인증 관련 상수
     */
    object Auth {

        const val LOGIN_TOKEN_EXPIRY_MINUTES = 10L
        const val VERIFICATION_EXPIRY_MINUTES = 30L
    }

    /**
     * 시간 관련 상수 (밀리초)
     */
    object Time {
        const val MINUTE_IN_MILLIS = 60 * 1000L
        const val HOUR_IN_MILLIS = 60 * 60 * 1000L
        const val DAY_IN_MILLIS = 24 * 60 * 60 * 1000L
    }
}
