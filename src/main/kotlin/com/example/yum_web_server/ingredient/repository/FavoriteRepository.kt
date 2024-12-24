package com.example.yum_web_server.ingredient.repository

import com.example.yum_web_server.ingredient.entity.Favorite
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface FavoriteRepository : CoroutineCrudRepository<Favorite, IngredientCategory> {
    @Query("SELECT * FROM favorite")
    suspend fun findAllFavorites() : List<Favorite>

    @Query("SELECT category FROM favorite WHERE category = :category")
    suspend fun findByCategory(category: IngredientCategory) : Favorite?

    @Query("DELETE FROM favorite WHERE category = :category")
    suspend fun deleteByCategory(category: IngredientCategory) : Unit
}