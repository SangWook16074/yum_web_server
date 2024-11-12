package com.example.yum_web_server.ingredient.controller

import com.example.yum_web_server.ingredient.dto.IngredientResponseDto
import com.example.yum_web_server.ingredient.service.IngredientService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ingredients")
class IngredientController(
    private val ingredientService: IngredientService
) {
    /**
     * 나의 재료 불러오기 Api
     */
    @GetMapping
    private fun getMyIngredients() : ResponseEntity<List<IngredientResponseDto>> {
        val result = ingredientService.getMyIngredient()
        return ResponseEntity.ok(result)
    }
}