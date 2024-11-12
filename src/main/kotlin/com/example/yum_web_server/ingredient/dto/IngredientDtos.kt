package com.example.yum_web_server.ingredient.dto

import com.example.yum_web_server.ingredient.entity.Ingredient
import com.example.yum_web_server.ingredient.enums.IngredientCategory

data class IngredientRequestDto(
    val id : Long?,
    val name : String,
    val isFreezed : Boolean,
    val category: IngredientCategory,
) {
    fun toEntity() : Ingredient = Ingredient(
        id = null,
        name = name,
        isFreezed = isFreezed,
        isFavorite = false,
        category = category,
    )
}

data class IngredientResponseDto(
    val id : Long?,
    val name : String,
    val isFreezed : Boolean,
    val isFavorite : Boolean,
    val category: IngredientCategory,
)
