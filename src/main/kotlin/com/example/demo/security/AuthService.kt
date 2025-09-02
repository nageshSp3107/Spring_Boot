package com.example.demo.security

import com.example.demo.database.model.RefreshToken
import com.example.demo.database.model.User
import com.example.demo.database.repository.RefreshTokenRepository
import com.example.demo.database.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )

    fun register(email: String, password:String): User {
            return userRepository.save(
                User(email = email,
                    hashedPassword = hashEncoder.encode(password))
            )
    }

    fun login(email: String, password:String): TokenPair {
        val user = userRepository.findByEmail(email) ?: throw BadCredentialsException("The email doesn't exist")
        if (!hashEncoder.matches(password, user.hashedPassword)){
            throw BadCredentialsException("The password doesn't match")
        }
        val accessToken = jwtService.generateAccessToken(user.id.toHexString())
        val refreshToken = jwtService.generateRefreshToken(user.id.toHexString())

        storeRefreshToken(user.id, refreshToken)

        return TokenPair(accessToken, refreshToken)
    }

    @Transactional
    fun refresh(refreshToken: String): TokenPair {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw BadCredentialsException("The refresh token doesn't match")
        }
        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findByIdOrNull(ObjectId(userId))
        ?: throw BadCredentialsException("The user doesn't exist")

        val hashed = hashToken(refreshToken)

        refreshTokenRepository.findByUserIdAndHashedToken(user.id, hashed) ?: throw BadCredentialsException("The user doesn't have access")

        refreshTokenRepository.deleteByUserIdAndHashedToken(user.id, hashed)
        val accessToken = jwtService.generateAccessToken(user.id.toHexString())
        val refreshToken = jwtService.generateRefreshToken(user.id.toHexString())

        storeRefreshToken(user.id, refreshToken)

        return TokenPair(accessToken, refreshToken)
    }

    private fun storeRefreshToken(userId: ObjectId, rawRefreshToken: String) {
        val hashed = hashToken(rawRefreshToken)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiryAt = Instant.now().plusMillis(expiryMs)
        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                expiresAt = expiryAt,
                hashedToken = hashed
            )
        )
    }

    private fun hashToken(token: String): String{
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes  = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}