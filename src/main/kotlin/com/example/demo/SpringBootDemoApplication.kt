package com.example.demo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootApplication
class SpringBootDemoApplication{

	@Bean
	fun checkDb(mongoTemplate: MongoTemplate) = CommandLineRunner {
		println("📌 Connected to DB: ${mongoTemplate.db.name}")
	}
}

fun main(args: Array<String>) {
	runApplication<SpringBootDemoApplication>(*args)
}

@Bean
fun testMongo(mongoTemplate: MongoTemplate) = CommandLineRunner {
	println("📌 Using database: ${mongoTemplate.db.name}")
}
