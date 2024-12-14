package com.example.yum_web_server.ingredient.repository

import com.example.yum_web_server.ingredient.entity.Ingredient
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import java.time.LocalDate

@DataJpaTest
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
        id = null,
        name = "egg",
        isFreezed = false,
        isFavorite = false,
        category = IngredientCategory.egg,
        startAt = LocalDate.of(2024, 11, 12),
        endAt = LocalDate.of(2024, 11, 19),
    )

    @BeforeEach
    fun setup() {
        ingredientRepository.save(ingredient)
    }

    @AfterEach
    fun tearDown() {
        ingredientRepository.deleteAll()
    }

    @Test
    fun `재료 조회 테스트`() {

        val result = ingredientRepository.findAll()
        assertThat(result).hasSize(1)
    }

    @Test
    fun `재료 생성 테스트`() {

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

    @Test
    fun `재료 삭제 테스트`() {
        val ingredientToDelete = ingredientRepository.save(ingredient)

        ingredientRepository.deleteById(ingredientToDelete.id!!)

        val result = ingredientRepository.findById(ingredientToDelete.id!!)
        assertThat(result).isEmpty
    }
}