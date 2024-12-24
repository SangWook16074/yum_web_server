package com.example.yum_web_server.ingredient.repository
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.r2dbc.core.DatabaseClient

@DataR2dbcTest
class FavoriteRepositoryTest @Autowired constructor (
    private val favoriteRepository: FavoriteRepository,
    private val databaseClient: DatabaseClient
) : StringSpec({
    beforeTest {
        runBlocking {
            databaseClient.sql("DELETE FROM favorite").then().block()
            databaseClient.sql("INSERT INTO favorite (category) VALUES ('egg')").then().block()
        }
    }
    afterTest {
        clearAllMocks()
    }
    coroutineTestScope = true
    "findByCategory로 원하는 카테고리를 찾을 수 있다." {
        val result = favoriteRepository.findByCategory(IngredientCategory.egg)
        result shouldNotBe null
        result!!.category shouldBe IngredientCategory.egg
    }

    "deleteByCategory로 원하는 카테고리를 삭제할 수 있다." {
        val after = favoriteRepository.findAllFavorites()
        after.size shouldBe 1
        favoriteRepository.deleteByCategory(IngredientCategory.egg)
        val result = favoriteRepository.findAllFavorites()
        result.size shouldBe 0
    }
})