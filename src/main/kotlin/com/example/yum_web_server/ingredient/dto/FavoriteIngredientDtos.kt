package com.example.yum_web_server.ingredient.dto

import com.example.yum_web_server.common.annotation.ValidEnum
import com.example.yum_web_server.ingredient.entity.Favorite
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import com.fasterxml.jackson.annotation.JsonProperty

data class FavoriteRequestDto(
    @field:ValidEnum(enumClass = IngredientCategory::class, message = "잘못된 재료 카테고리입니다!")
    @JsonProperty("category")
    private val _category : String?,
) {

    val category : IngredientCategory
        get() =  IngredientCategory.valueOf(_category!!)

    fun toEntity() : Favorite {
        val favorite = Favorite(category = category)
        return favorite
    }
}

data class FavoriteResponseDto(
    val category: IngredientCategory
)