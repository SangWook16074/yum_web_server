package com.example.yum_web_server.ingredient.repository

import com.example.yum_web_server.ingredient.entity.Ingredient
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import java.time.LocalDate


@DataR2dbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IngredientRepositoryTest(
    private val ingredientRepository: IngredientRepository
) : BehaviorSpec({
    Given("재료가 등록되어 있는 경우에") {
        When("재료를 호출하면") {
            coroutineTestScope = true
            beforeTest {
                val ingredient : Ingredient = Ingredient(
                    name = "egg",
                    isFreezed = false,
                    isFavorite = false,
                    category = IngredientCategory.egg,
                    startAt = LocalDate.of(2024, 11, 12),
                    endAt = LocalDate.of(2024, 11, 19),
                )
                ingredientRepository.save(ingredient)
            }
            afterTest {
                ingredientRepository.deleteAll()
            }
            Then("1개의 결과가 반환된다.") {
                val result = ingredientRepository.findAllIngredients()
                result.let { ingredients ->
                    ingredients.size shouldBe 1
                    val first = ingredients[0]
                    first.name shouldBe "egg"
                    first.category shouldBe IngredientCategory.egg
                    first.isFreezed shouldBe false
                    first.isFavorite shouldBe false
                    first.startAt shouldBe LocalDate.of(2024, 11, 12)
                    first.endAt shouldBe LocalDate.of(2024, 11, 19)
                }
            }
        }
    }
    Given("재료가 등록되지 않은 경우에") {
        When("재료를 호출하면") {
            coroutineTestScope = true
            Then("0개가 반환된다.") {
                val result = ingredientRepository.findAllIngredients()
                result.size shouldBe 0
            }
        }
    }
})