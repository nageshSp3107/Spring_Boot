package com.example.demo.controllers

import com.example.demo.database.model.User
import com.example.demo.security.AuthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    val authService: AuthService,
) {
    data class AuthRequest(val email: String, val password: String)
    data class AuthRefreshToken(val refreshToken: String)

    @PostMapping("/register")
    fun register(@RequestBody auth: AuthRequest): User {
        return authService.register(auth.email, auth.password)
    }

    @PostMapping("/login")
    fun login(@RequestBody auth: AuthRequest): AuthService.TokenPair = authService.login(auth.email, auth.password)

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody token: AuthRefreshToken): AuthService.TokenPair = authService.refresh(token.refreshToken)
}