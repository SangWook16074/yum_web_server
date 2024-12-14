package com.example.yum_web_server.ingredient.repository

import com.example.yum_web_server.ingredient.entity.Ingredient
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import java.time.LocalDate

@DataR2dbcTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = ["classpath:application-test.yml"])
class IngredientRepositoryTest @Autowired constructor(
    private val ingredientRepository: IngredientRepository,
) {
    /**
     * 테스트 데이터
     */
    val ingredient : Ingredient = Ingredient(
        name = "egg",
        isFreezed = false,
        isFavorite = false,
        category = IngredientCategory.egg,
        startAt = LocalDate.of(2024, 11, 12),
        endAt = LocalDate.of(2024, 11, 19),
    )

    @BeforeEach
    fun setup() : Unit = runBlocking {
        ingredientRepository.save(ingredient)
    }

    @AfterEach
    fun tearDown() = runBlocking {
        ingredientRepository.deleteAll()
    }

    @Test
    fun `재료 조회 테스트`() = runTest {
        val result = ingredientRepository.findAllIngredients()

        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("egg")
        assertThat(result[0].category).isEqualTo(IngredientCategory.egg)
        assertThat(result[0].startAt).isEqualTo(LocalDate.of(2024, 11, 12))
        assertThat(result[0].endAt).isEqualTo(LocalDate.of(2024, 11, 19))
    }

    @Test
    fun `재료 생성 테스트`() = runTest {

        ingredientRepository.save(ingredient)
        val result = ingredientRepository.save(ingredient)

        with(result) {
            assertThat(name).isEqualTo("egg")
            assertThat(isFreezed).isEqualTo(false)
            assertThat(isFavorite).isEqualTo(false)
            assertThat(startAt).isEqualTo(LocalDate.of(2024, 11, 12))
            assertThat(endAt).isEqualTo(LocalDate.of(2024, 11, 19))
            assertThat(category).isEqualTo(IngredientCategory.egg)
        }

    }
}