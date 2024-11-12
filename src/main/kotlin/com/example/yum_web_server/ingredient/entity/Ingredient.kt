package com.example.yum_web_server.ingredient.entity

import com.example.yum_web_server.ingredient.dto.IngredientResponseDto
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Ingredient(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Long? = null,

    @Column(length = 100)
    var name : String,

    @Column(length = 5)
    var isFreezed : Boolean,

    @Column(length = 30)
    var category : IngredientCategory,

    @Column
    var isFavorite : Boolean,
) {
    fun toResponse() : IngredientResponseDto = IngredientResponseDto(
        id = id,
        name = name,
        isFreezed = isFreezed,
        isFavorite = isFavorite,
        category = category,
    )
}