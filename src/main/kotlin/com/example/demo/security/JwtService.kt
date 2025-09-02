package com.example.demo.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.util.Base64
import java.util.Date

@Service
class JwtService(
    @Value("\${jwt.secret}") private val jwtSecretKey: String
) {
    private val base = jwtSecretKey
    private val sha256 = MessageDigest.getInstance("SHA-256").digest(base.toByteArray())
    private val base64Key = Base64.getEncoder().encodeToString(sha256)
    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Key))

    private val accessTokenValidityMs = 15L * 60 * 1000
    val refreshTokenValidityMs = 30L * 24 * 60 * 60 * 1000

    private fun generateToken(
        userId: String,
        type: String,
        expiry: Long,
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + expiry)
        return Jwts.builder()
            .subject(userId)
            .claim("type", type)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    fun generateAccessToken(userId: String): String{
        return generateToken(userId, "access",accessTokenValidityMs)
    }

    fun generateRefreshToken(userId: String): String{
        return generateToken(userId, "refresh",refreshTokenValidityMs)
    }

    fun validateAccessToken(token: String): Boolean{
        val claims = parseAllClaims(token) ?: return false
        val typeToken = claims["type"] as? String ?: return false
        return typeToken == "access"
    }

    fun validateRefreshToken(token: String): Boolean{
        val claims = parseAllClaims(token) ?: return false
        val typeToken = claims["type"] as? String ?: return false
        return typeToken == "refresh"
    }

    fun getUserIdFromToken(token: String):String{
        val rawToken  = if (token.startsWith("Bearer ")) token.removePrefix("Bearer ") else token
        val claims = parseAllClaims(rawToken) ?: throw IllegalArgumentException("invalid token")
        return claims.subject
    }

    private fun parseAllClaims(token: String): Claims? {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
        }catch (e: Exception){
            null
        }
    }
}