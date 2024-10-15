package com.example.yum_web_server

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class YumWebServerApplication

fun main(args: Array<String>) {
	runApplication<YumWebServerApplication>(*args)
}