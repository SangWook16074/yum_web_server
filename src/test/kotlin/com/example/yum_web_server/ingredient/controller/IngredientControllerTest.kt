package com.example.yum_web_server.ingredient.controller

import com.example.yum_web_server.ingredient.dto.IngredientResponseDto
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import com.example.yum_web_server.ingredient.service.IngredientService
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import com.ninjasquad.springmockk.MockkBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(IngredientController::class)
@AutoConfigureMockMvc(addFilters = false)
class IngredientControllerTest {

    @Autowired
    private lateinit var mockMvc : MockMvc

    @MockkBean
    private lateinit var ingredientService : IngredientService

    val ingredients : List<IngredientResponseDto> = listOf(
        IngredientResponseDto(
            id = 1,
            name = "egg",
            isFreezed = false,
            isFavorite = false,
            category = IngredientCategory.egg
        ),
        IngredientResponseDto(
            id = 2,
            name = "beef",
            isFreezed = false,
            isFavorite = false,
            category = IngredientCategory.beef
        ),
    )

    @Test
    fun `사용자 재료 조회 테스트 성공시 응답코드 200 반환`() {

        every { ingredientService.getMyIngredient() } returns ingredients

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ingredients"))
            .andExpect(status().isOk)

        verify(exactly = 1) { ingredientService.getMyIngredient() }
    }
}