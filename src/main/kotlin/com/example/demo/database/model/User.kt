package com.example.demo.database.model

import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class User (
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
)