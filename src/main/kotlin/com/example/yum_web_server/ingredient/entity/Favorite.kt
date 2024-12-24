package com.example.yum_web_server.ingredient.entity

import com.example.yum_web_server.ingredient.dto.FavoriteResponseDto
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table
data class Favorite(
    @Id
    @Column
    val category : IngredientCategory,
) : Persistable<IngredientCategory> {

    override fun isNew(): Boolean {
        return true
    }

    override fun getId(): IngredientCategory = this.category

    fun toResponse() : FavoriteResponseDto = FavoriteResponseDto(category = category)
}