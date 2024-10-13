package com.example.yum_web_server

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class YumWebServerApplication

fun main(args: Array<String>) {
	// .env 파일 로드
	val dotenv = dotenv()

	// 환경 변수 가져오기
	val dbUrl = dotenv["DB_URL"]
	val dbUsername = dotenv["DB_USERNAME"]
	val dbPassword = dotenv["DB_PASSWORD"]

	// 환경 변수를 확인하기 위한 코드
	println("DB URL: $dbUrl")
	println("DB Username: $dbUsername")
	println("DB Password: $dbPassword")

	runApplication<YumWebServerApplication>(*args)
}