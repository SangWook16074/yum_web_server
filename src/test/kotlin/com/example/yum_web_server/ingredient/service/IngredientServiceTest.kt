package com.example.yum_web_server.ingredient.service

import com.example.yum_web_server.ingredient.dto.IngredientRequestDto
import com.example.yum_web_server.ingredient.entity.Ingredient
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import com.example.yum_web_server.ingredient.repository.IngredientRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate


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
                category = IngredientCategory.egg,
                startAt = LocalDate.of(2024, 11, 12),
                endAt = LocalDate.of(2024, 11, 19),
            ),
            Ingredient(
                id = 2,
                name = "beef",
                isFreezed = false,
                isFavorite = false,
                category = IngredientCategory.beef,
                startAt = LocalDate.of(2024, 11, 12),
                endAt = LocalDate.of(2024, 11, 19),
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

    @Test
    fun `재료 생성 테스트`() {
        val ingredient = Ingredient(
            id = 1,
            name = "egg",
            isFreezed = false,
            isFavorite = false,
            category = IngredientCategory.egg,
            startAt = LocalDate.of(2024, 11, 12),
            endAt = LocalDate.of(2024, 11, 19),
        )

        val ingredientRequestDto = IngredientRequestDto(
            _name = "egg",
            _isFreezed = false,
            _category = IngredientCategory.egg,
            _startAt = "2024-11-11",
            _endAt = "2024-11-19",
        )
        every { ingredientRepository.save(any()) } returns ingredient

        val result = ingredientService.createIngredient(ingredientRequestDto)

        verify(exactly = 1) { ingredientRepository.save(any()) }

        assertThat(result.name).isEqualTo("egg")
        assertThat(result.isFreezed).isFalse()
        assertThat(result.isFavorite).isFalse()
        assertThat(result.category).isEqualTo(IngredientCategory.egg)
        assertThat(result.startAt.year).isEqualTo(2024)
        assertThat(result.startAt.monthValue).isEqualTo(11)
        assertThat(result.startAt.dayOfMonth).isEqualTo(12)

    }
}