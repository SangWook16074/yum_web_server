package com.example.yum_web_server.ingredient.controller

import com.example.yum_web_server.ingredient.dto.IngredientRequestDto
import com.example.yum_web_server.ingredient.dto.IngredientResponseDto
import com.example.yum_web_server.ingredient.service.IngredientService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Ingredient 서버" ,description = "재료 관련 Api",)
@RestController
@RequestMapping("/api/ingredients")
class IngredientController(
    private val ingredientService: IngredientService
) {
    /**
     * 나의 재료 불러오기 Api
     */
    @Operation(description = "나의 재료 불러오기 Api")
    @GetMapping
    suspend fun getMyIngredients() : ResponseEntity<List<IngredientResponseDto>> {
        val result = ingredientService.getMyIngredient()
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    /**
     * 나의 재료 생성 Api
     */
    @Operation(description = "나의 재료 생성 Api")
    @PostMapping
    suspend fun createIngredient(@RequestBody @Valid ingredientRequestDto: IngredientRequestDto)
    : ResponseEntity<IngredientResponseDto>
    {
        val result = ingredientService.createIngredient(ingredientRequestDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(result)
    }

    /**
     * 나의 재료 삭제 Api
     */
    @Operation(description = "나의 재료 삭제 Api")
    @DeleteMapping("/{id}")
    suspend fun deleteIngredient(@PathVariable id: Long): ResponseEntity<Void> {
        ingredientService.deleteIngredient(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}