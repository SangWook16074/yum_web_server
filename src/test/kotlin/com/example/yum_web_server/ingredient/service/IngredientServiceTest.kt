package com.example.yum_web_server.ingredient.service

import com.example.yum_web_server.ingredient.dto.IngredientRequestDto
import com.example.yum_web_server.ingredient.entity.Ingredient
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import com.example.yum_web_server.ingredient.repository.IngredientRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import java.time.LocalDate

class IngredientServiceTest : BehaviorSpec({
    val ingredientRepository = mockk<IngredientRepository>()
    val ingredientService = IngredientService(ingredientRepository)
    Given("Ingredient Service에서") {
        coroutineDebugProbes = true
        afterTest {
            clearAllMocks()
        }
        When("재료가 이미 있는 경우에") {
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
            coEvery { ingredientRepository.findAllIngredients() } returns ingredients
            Then("getMyIngredient()는 2개의 재료가 반환된다.") {
                val result = ingredientService.getMyIngredient()
                coVerify(exactly = 1) { ingredientRepository.findAllIngredients() }
                result.size shouldBe 2
                with(result[0]) {
                    id shouldBe 1L
                    name shouldBe "egg"
                    isFreezed shouldBe false
                    isFavorite shouldBe false
                    category shouldBe IngredientCategory.egg
                }
                with(result[1]) {
                    id shouldBe 2L
                    name shouldBe "beef"
                    isFreezed shouldBe false
                    isFavorite shouldBe false
                    category shouldBe IngredientCategory.beef
                }
            }

            Then("createIngredient()는 생성한 재료를 반환한다.") {
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
                    _category = "egg",
                    _startAt = "2024-11-12",
                    _endAt = "2024-11-19",
                )
                coEvery { ingredientRepository.save(any()) } returns ingredient
                val result = ingredientService.createIngredient(ingredientRequestDto)
                coVerify(exactly = 1) { ingredientRepository.save(any()) }
                with(result) {
                    name shouldBe "egg"
                    isFreezed shouldBe false
                    isFavorite shouldBe false
                    category shouldBe IngredientCategory.egg
                    startAt shouldBe LocalDate.of(2024, 11, 12)
                    endAt shouldBe LocalDate.of(2024, 11, 19)
                }
            }
        }

        When("재료가 없는 경우에") {
            coEvery { ingredientRepository.findAllIngredients() } returns emptyList()
            Then("getMyIngredients()는 0개의 재료를 반환한다") {
                val result = ingredientService.getMyIngredient()
                coVerify(exactly = 1) { ingredientRepository.findAllIngredients() }
                result.size shouldBe 0
            }
        }
    }
})