package com.example.demo.controllers

import com.example.demo.database.model.User
import com.example.demo.database.repository.UserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("users")
class UsersController(
    val userRepository: UserRepository
) {

    @GetMapping
    fun findAll(): List<User> = userRepository.findAll()

    @PostMapping
    fun save(@RequestBody user: User): User = userRepository.save(user)

    @GetMapping("/")
    fun getByEmail(email: String): User? = userRepository.findByEmail(email)
}