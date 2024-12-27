package com.example.yum_web_server.ingredient.service

import com.example.yum_web_server.ingredient.dto.FavoriteRequestDto
import com.example.yum_web_server.ingredient.dto.IngredientRequestDto
import com.example.yum_web_server.ingredient.entity.Favorite
import com.example.yum_web_server.ingredient.entity.Ingredient
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import com.example.yum_web_server.ingredient.repository.FavoriteRepository
import com.example.yum_web_server.ingredient.repository.IngredientRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import java.lang.RuntimeException
import java.time.LocalDate

class IngredientServiceTest : BehaviorSpec({
    val ingredientRepository = mockk<IngredientRepository>()
    val favoriteRepository = mockk<FavoriteRepository>()
    val ingredientService = IngredientService(ingredientRepository, favoriteRepository)
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
                    category = IngredientCategory.egg,
                    startAt = LocalDate.of(2024, 11, 12),
                    endAt = LocalDate.of(2024, 11, 19),
                ),
                Ingredient(
                    id = 2,
                    name = "beef",
                    isFreezed = false,
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
                    category shouldBe IngredientCategory.egg
                }
                with(result[1]) {
                    id shouldBe 2L
                    name shouldBe "beef"
                    isFreezed shouldBe false
                    category shouldBe IngredientCategory.beef
                }
            }

            Then("saveIngredient()는 생성한 재료를 반환한다.") {
                val ingredient = Ingredient(
                    id = 1,
                    name = "egg",
                    isFreezed = false,
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
                val result = ingredientService.saveIngredient(ingredientRequestDto)
                coVerify(exactly = 1) { ingredientRepository.save(any()) }
                with(result) {
                    name shouldBe "egg"
                    isFreezed shouldBe false
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

        When("즐겨찾기 재료가 있으면") {
            val favorites = listOf(
                Favorite(category = IngredientCategory.egg),
                Favorite(category = IngredientCategory.beef),
                Favorite(category = IngredientCategory.drink),
            )
            val newFavorite = Favorite(category = IngredientCategory.processed_meat)
            coEvery { favoriteRepository.findAllFavorites() } returns favorites
            Then("즐겨찾기 재료들을 모두 반환한다.") {
                val result = ingredientService.getMyFavoriteIngredients()
                coVerify(exactly = 1) { favoriteRepository.findAllFavorites() }
                result.size shouldBe 3
                result[0].category shouldBe IngredientCategory.egg
                result[1].category shouldBe IngredientCategory.beef
                result[2].category shouldBe IngredientCategory.drink
            }

            Then("이미 있는 재료는 추가하지 못한다.") {
                coEvery { favoriteRepository.save(any()) } returns newFavorite
                coEvery { favoriteRepository.findByCategory(any()) } returns Favorite(category = IngredientCategory.egg)
                val conflictFavorite = FavoriteRequestDto(_category = "egg")
                val result = shouldThrow<RuntimeException> {
                    ingredientService.createNewFavorite(conflictFavorite)
                }
                coVerify(exactly = 1) { favoriteRepository.findByCategory(any()) }
                coVerify(exactly = 0) { favoriteRepository.save(any()) }
                result.message shouldBe "이미 추가된 재료입니다!"

            }

            Then("새로운 재료는 추가할 수 있다.") {
                coEvery { favoriteRepository.save(any()) } returns newFavorite
                coEvery { favoriteRepository.findByCategory(any()) } returns null
                val request = FavoriteRequestDto(_category = "processed_meat")
                val result = ingredientService.createNewFavorite(request)
                coVerify(exactly = 1) { favoriteRepository.findByCategory(any()) }
                coVerify(exactly = 1) { favoriteRepository.save(any()) }
                result.category shouldBe IngredientCategory.processed_meat
            }

            Then("이미 있는 재료는 삭제할 수 있다.") {
                coJustRun { favoriteRepository.deleteByCategory(any()) }
                val request = FavoriteRequestDto(_category = "egg")
                val result = ingredientService.deleteFavorite(request)
                result shouldBe "삭제되었습니다!"
            }
        }
    }
})