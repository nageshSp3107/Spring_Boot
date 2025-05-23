package com.example.demo.security

import org.apache.catalina.security.SecurityConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {
          @Bean
    fun filerChain(httpSecurity: HttpSecurity): SecurityFilterChain{
        return httpSecurity
            .csrf { csrfConfigurer -> csrfConfigurer.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .build()
    }
}