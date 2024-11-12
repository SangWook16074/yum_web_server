package com.example.yum_web_server.ingredient.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = ["classpath:application-test.yml"])
class IngredientRepositoryTest @Autowired constructor(
    private val ingredientRepository: IngredientRepository,
) {

    @Test
    fun `재료 조회 테스트`() {

        val result = ingredientRepository.findAll()

        assertThat(result.size).isEqualTo(0)
    }
}