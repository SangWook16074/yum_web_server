package com.example.yum_web_server.ingredient.controller

import com.example.yum_web_server.ingredient.dto.FavoriteRequestDto
import com.example.yum_web_server.ingredient.dto.FavoriteResponseDto
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
    private suspend fun getMyIngredients() : ResponseEntity<List<IngredientResponseDto>> {
        val result = ingredientService.getMyIngredient()
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    /**
     * 나의 재료 생성 Api
     */
    @Operation(description = "나의 재료 생성 Api")
    @PostMapping
    private suspend fun createIngredient(@RequestBody @Valid ingredientRequestDto: IngredientRequestDto)
    : ResponseEntity<IngredientResponseDto>
    {
        val result = ingredientService.saveIngredient(ingredientRequestDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(result)
    }

    /**
     * 나의 재료 수정 Api
     */
    @Operation(description = "나의 재료 수정 Api")
    @PutMapping
    private suspend fun updateIngredient(@RequestBody @Valid ingredientRequestDto: IngredientRequestDto)
    : ResponseEntity<IngredientResponseDto> {
        val result = ingredientService.saveIngredient(ingredientRequestDto)
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    /**
     * 나의 재료 삭제 Api
     */
    @Operation(description = "나의 재료 삭제 Api")
    @DeleteMapping("/{id}")
    private suspend fun deleteIngredient(@PathVariable id: Long): ResponseEntity<Void> {
        ingredientService.deleteIngredient(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    /**
     * 나의 즐겨찾기 재료 불러오기 Api
     */
    @Operation(description = "나의 즐겨찾기 재료 불러오기 Api")
    @GetMapping("/favorites")
    private suspend fun getMyFavoriteIngredients() : ResponseEntity<List<FavoriteResponseDto>> {
        val result = ingredientService.getMyFavoriteIngredients()
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    /**
     * 나의 즐겨찾기 재료 추가 Api
     */
    @Operation(description = "즐겨찾기 재료 추가하기 Api")
    @PostMapping("/favorites")
    private suspend fun createNewFavorite(@Valid @RequestBody favoriteRequestDto: FavoriteRequestDto)
    : ResponseEntity<FavoriteResponseDto> {
        val result = ingredientService.createNewFavorite(favoriteRequestDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(result)
    }

    /**
     * 나의 즐겨찾기 재료 삭제 Api
     */
    @Operation(description = "즐겨찾기 재료 삭제하기 Api")
    @DeleteMapping("favorites")
    private suspend fun deleteFavorite(@Valid @RequestBody favoriteRequestDto: FavoriteRequestDto)
    : ResponseEntity<String>
    {
        val result = ingredientService.deleteFavorite(favoriteRequestDto)
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }
}