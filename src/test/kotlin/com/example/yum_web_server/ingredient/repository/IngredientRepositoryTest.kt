package com.example.yum_web_server.ingredient.repository

import com.example.yum_web_server.ingredient.entity.Ingredient
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.r2dbc.core.DatabaseClient
import java.time.LocalDate

@DataR2dbcTest
class IngredientRepositoryTest @Autowired constructor(
    private val ingredientRepository: IngredientRepository,
    private val databaseClient: DatabaseClient,
) : StringSpec({
    coroutineTestScope = true
    beforeTest {
        runBlocking {
            databaseClient.sql("DELETE FROM ingredient").then().block()
            databaseClient.sql("INSERT INTO ingredient " +
                    "VALUES (1, 'egg', false, 'egg', '2024-11-12', '2024-11-19')").then().block()
        }
    }
    afterTest {
        clearAllMocks()
    }
    "save 메소드를 통해서 새로운 재료를 생성할 수 있다." {
        val newIngredient = Ingredient(
            name = "beer",
            isFreezed = false,
            category = IngredientCategory.beer,
            startAt = LocalDate.of(2024, 11, 12),
            endAt = LocalDate.of(2024, 11, 19)
        )
        val result = ingredientRepository.save(newIngredient)
        with(result) {
            name shouldBe "beer"
            isFreezed shouldBe false
            category shouldBe IngredientCategory.beer
            startAt shouldBe LocalDate.of(2024, 11, 12)
            endAt shouldBe LocalDate.of(2024, 11, 19)
        }
    }

    "save 메소드를 통해서 기존의 재료를 수정할 수 있다." {
        val updatedIngredient = Ingredient(
            id = 1L,
            name = "changed egg",
            isFreezed = true,
            category = IngredientCategory.beer,
            startAt = LocalDate.of(2024, 11, 12),
            endAt = LocalDate.of(2024, 11, 19)
        )
        val prevIngredient = ingredientRepository.findById(1L)!!
        with(prevIngredient) {
            id shouldBe 1L
            name shouldBe "egg"
            isFreezed shouldBe false
            category shouldBe IngredientCategory.egg
            startAt = LocalDate.of(2024, 11, 12)
            endAt = LocalDate.of(2024, 11, 19)
        }
        ingredientRepository.save(updatedIngredient)
        val result = ingredientRepository.findById(1L)!!
        with(result) {
            id shouldBe 1L
            name shouldBe "changed egg"
            isFreezed shouldBe true
            category shouldBe IngredientCategory.beer
            startAt shouldBe LocalDate.of(2024, 11, 12)
            endAt shouldBe LocalDate.of(2024, 11, 19)
        }
    }

    "save 메소드를 통해서 Long이 아닌 Int 타입도 똑같이 적용된다." {
        val updatedIngredient = Ingredient(
            id = 1,
            name = "changed egg",
            isFreezed = true,
            category = IngredientCategory.beer,
            startAt = LocalDate.of(2024, 11, 12),
            endAt = LocalDate.of(2024, 11, 19)
        )
        val prevIngredient = ingredientRepository.findById(1L)!!
        with(prevIngredient) {
            id shouldBe 1L
            name shouldBe "egg"
            isFreezed shouldBe false
            category shouldBe IngredientCategory.egg
            startAt = LocalDate.of(2024, 11, 12)
            endAt = LocalDate.of(2024, 11, 19)
        }
        ingredientRepository.save(updatedIngredient)
        val result = ingredientRepository.findById(1L)!!
        with(result) {
            id shouldBe 1L
            name shouldBe "changed egg"
            isFreezed shouldBe true
            category shouldBe IngredientCategory.beer
            startAt shouldBe LocalDate.of(2024, 11, 12)
            endAt shouldBe LocalDate.of(2024, 11, 19)
        }
    }

})