package com.example.yum_web_server.ingredient.service

import com.example.yum_web_server.ingredient.dto.IngredientResponseDto
import com.example.yum_web_server.ingredient.repository.IngredientRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class IngredientService(
    private val ingredientRepository: IngredientRepository
) {
    /**
     * 나의 재료 불러오기
     */
    fun getMyIngredient() : List<IngredientResponseDto> {
        val result = ingredientRepository.findAll()
        return result.map { it.toResponse() }
    }
}