package com.example.yum_web_server.ingredient.controller

import com.example.yum_web_server.ingredient.dto.IngredientRequestDto
import com.example.yum_web_server.ingredient.dto.IngredientResponseDto
import com.example.yum_web_server.ingredient.service.IngredientService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    /**
     * 나의 재료 생성 Api
     */
    @PostMapping
    private fun createIngredient(@Valid @RequestBody ingredientRequestDto: IngredientRequestDto)
    : ResponseEntity<IngredientResponseDto> {
        val result = ingredientService.createIngredient(ingredientRequestDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(result)
    }
}