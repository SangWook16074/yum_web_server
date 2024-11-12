package com.example.yum_web_server.ingredient.service

import com.example.yum_web_server.ingredient.entity.Ingredient
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import com.example.yum_web_server.ingredient.repository.IngredientRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class IngredientServiceTest {
    private val ingredientRepository : IngredientRepository = mockk()
    private val ingredientService : IngredientService = IngredientService(ingredientRepository = ingredientRepository)


    @Test
    fun `재료 조회 테스트`() {
        val ingredients : List<Ingredient> = listOf(
            Ingredient(
                id = 1,
                name = "egg",
                isFreezed = false,
                isFavorite = false,
                category = IngredientCategory.egg
            ),
            Ingredient(
                id = 2,
                name = "beef",
                isFreezed = false,
                isFavorite = false,
                category = IngredientCategory.beef
            ),
        )
        every { ingredientRepository.findAll() } returns ingredients

        val result = ingredientService.getMyIngredient()

        verify(exactly = 1) { ingredientRepository.findAll() }

        assertThat(result.size).isEqualTo(2)
        /**
         * 첫번째 재료 검증
         */
        assertThat(result.first().id).isEqualTo(1L)
        assertThat(result.first().name).isEqualTo("egg")
        assertThat(result.first().isFreezed).isEqualTo(false)
        assertThat(result.first().isFavorite).isEqualTo(false)
        assertThat(result.first().category).isEqualTo(IngredientCategory.egg)
        /**
         * 두번째 재료 검증
         */
        assertThat(result.last().id).isEqualTo(2L)
        assertThat(result.last().name).isEqualTo("beef")
        assertThat(result.last().isFreezed).isEqualTo(false)
        assertThat(result.last().isFavorite).isEqualTo(false)
        assertThat(result.last().category).isEqualTo(IngredientCategory.beef)
    }
}