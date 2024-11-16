package com.example.yum_web_server.ingredient.dto

import com.example.yum_web_server.common.annotation.ValidEnum
import com.example.yum_web_server.ingredient.entity.Ingredient
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class IngredientRequestDto(
    @field:NotBlank(message = "이름을 입력하세요!")
    @JsonProperty("name")
    private var _name : String?,

    @JsonProperty("isFreezed")
    private var _isFreezed : Boolean? = false,

    @field:NotBlank(message = "카테고리를 입력하세요!")
    @field:ValidEnum(enumClass = IngredientCategory::class, message = "잘못된 재료 카테고리입니다!")
    @JsonProperty("category")
    private var _category: String?,

    @field:NotBlank(message = "시작날짜를 입력하세요!")
    @field:Pattern(
        regexp = "^([12]\\d{3})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])\$",
        message = "잘못된 날짜형식입니다!"
    )
    @JsonProperty("startAt")
    private var _startAt : String?,

    @field:NotBlank(message = "종료날짜를 입력하세요!")
    @field:Pattern(
        regexp = "^([12]\\d{3})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])\$",
        message = "잘못된 날짜형식입니다!"
    )
    @JsonProperty("endAt")
    private var _endAt : String?,
) {
    val name : String
        get() = _name!!
    val isFreezed : Boolean
        get() = _isFreezed!!
    val category: IngredientCategory
        get() = IngredientCategory.valueOf(_category!!)
    val startAt : LocalDate
        get() = _startAt!!.toLocalDate()
    val endAt : LocalDate
        get() = _endAt!!.toLocalDate()

    fun toEntity() : Ingredient = Ingredient(
        id = null,
        name = name,
        isFreezed = isFreezed,
        isFavorite = false,
        category = category,
        startAt = startAt,
        endAt = endAt,
    )

    private fun String.toLocalDate() : LocalDate =
        LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

data class IngredientResponseDto(
    val id : Long?,
    val name : String,
    val isFreezed : Boolean,
    val isFavorite : Boolean,
    val category: IngredientCategory,
    val startAt : LocalDate,
    val endAt: LocalDate,
)
