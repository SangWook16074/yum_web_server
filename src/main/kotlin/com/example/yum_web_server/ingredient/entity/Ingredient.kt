package com.example.yum_web_server.ingredient.entity

import com.example.yum_web_server.ingredient.dto.IngredientResponseDto
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("ingredient")
data class Ingredient(
    @Id
    val id : Long? = null,

    @Column
    var name : String,

    @Column("is_freezed")
    var isFreezed : Boolean,

    @Column("is_favorite")
    var isFavorite : Boolean,

    @Column
    var category : IngredientCategory,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column("start_at")
    var startAt : LocalDate,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column("end_at")
    var endAt : LocalDate,
) {
    fun toResponse() : IngredientResponseDto = IngredientResponseDto(
        id = id,
        name = name,
        isFreezed = isFreezed,
        isFavorite = isFavorite,
        category = category,
        startAt = startAt,
        endAt = endAt,
    )
}