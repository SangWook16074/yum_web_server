package com.example.yum_web_server.ingredient.repository

import com.example.yum_web_server.ingredient.entity.Ingredient
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface IngredientRepository : CoroutineCrudRepository<Ingredient, Long> {
    @Query("SELECT * FROM ingredient")
    suspend fun findAllIngredients(): List<Ingredient>
}