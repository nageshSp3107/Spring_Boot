package com.example.demo.database.repository

import com.example.demo.database.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun findByEmail(email: String): User?
    fun deleteByEmail(email: String)
}