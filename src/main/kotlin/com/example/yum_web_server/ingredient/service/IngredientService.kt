package com.example.yum_web_server.ingredient.service

import com.example.yum_web_server.ingredient.dto.IngredientRequestDto
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

    /**
     * 재료 추가하기
     */
    fun createIngredient(ingredientRequestDto: IngredientRequestDto) : IngredientResponseDto {
        val result = ingredientRepository.save(ingredientRequestDto.toEntity())
        return result.toResponse()
    }

    /**
     * 재료 삭제하기
     */
    fun deleteIngredient(id: Long) {
        ingredientRepository.deleteById(id)
    }


}