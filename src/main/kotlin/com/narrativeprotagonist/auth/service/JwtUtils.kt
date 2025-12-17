package com.narrativeprotagonist.auth.service

import com.narrativeprotagonist._global.config.JwtProperties
import com.narrativeprotagonist._global.constants.AppConstants.Jwt.CLAIM_NICKNAME
import com.narrativeprotagonist._global.enums.JwtTokenType
import com.narrativeprotagonist._global.exception.BusinessException
import com.narrativeprotagonist.user.domain.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtils(
    private val jwtProperties: JwtProperties
) {
    // Access Token Secret Key
    private val accessSecretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtProperties.accessSecret.toByteArray())
    }

    // Refresh Token Secret Key
    private val refreshSecretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtProperties.refreshSecret.toByteArray())
    }

    /**
     * 유저 정보로 JWT 토큰 생성
     */
    fun generateJwtToken(user: User,  now: Date, tokenType: JwtTokenType): String {
        val expirationTime = when (tokenType) {
            JwtTokenType.ACCESS -> jwtProperties.expiration
            JwtTokenType.REFRESH -> jwtProperties.refreshExpiration
        }
        val secretKey = when (tokenType) {
            JwtTokenType.ACCESS -> accessSecretKey
            JwtTokenType.REFRESH -> refreshSecretKey
        }
        val userId = user.id!!
        val expiryDate = Date(now.time + expirationTime)
        val token = Jwts.builder()
            .subject(userId)  // sub: userId
            .claim(CLAIM_NICKNAME, user.nickname)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey)
            .compact()
        return token
    }

    /**
     * JWT 토큰 검증 (Access 또는 Refresh 토큰 둘 다 검증 가능)
     */
    fun validateToken(token: String, tokenType: JwtTokenType = JwtTokenType.ACCESS): Boolean {
        val secretKey = when (tokenType) {
            JwtTokenType.ACCESS -> accessSecretKey
            JwtTokenType.REFRESH -> refreshSecretKey
        }
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
            true

        }
        // 만료시 만료 에러
        catch (e: io.jsonwebtoken.ExpiredJwtException) {
            if (tokenType == JwtTokenType.ACCESS)
                throw BusinessException.AccessTokenExpired;
            else
                throw BusinessException.RefreshTokenExpired;
        } catch (e: Exception) {
            false
        }
    }

    /**
     * JWT 토큰에서 userId 추출
     */
    fun getUserIdFromToken(token: String, tokenType: JwtTokenType = JwtTokenType.ACCESS): String {
        val secretKey = when (tokenType) {
            JwtTokenType.ACCESS -> accessSecretKey
            JwtTokenType.REFRESH -> refreshSecretKey
        }
        val claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims.subject
    }

    /**
     * JWT 토큰의 만료 시간 확인
     */
    fun getExpirationFromToken(token: String, tokenType: JwtTokenType = JwtTokenType.ACCESS): Date {
        val secretKey = when (tokenType) {
            JwtTokenType.ACCESS -> accessSecretKey
            JwtTokenType.REFRESH -> refreshSecretKey
        }
        val claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims.expiration
    }

    /**
     * 토큰 해싱 (SHA-256)
     */
    fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}
