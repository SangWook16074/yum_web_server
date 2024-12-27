package com.example.yum_web_server.ingredient.service

import com.example.yum_web_server.ingredient.dto.FavoriteRequestDto
import com.example.yum_web_server.ingredient.dto.FavoriteResponseDto
import com.example.yum_web_server.ingredient.dto.IngredientRequestDto
import com.example.yum_web_server.ingredient.dto.IngredientResponseDto
import com.example.yum_web_server.ingredient.repository.FavoriteRepository
import com.example.yum_web_server.ingredient.repository.IngredientRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IngredientService @Autowired(required = false) constructor (
    private val ingredientRepository: IngredientRepository,
    private val favoriteRepository: FavoriteRepository,
) {
    /**
     * 나의 재료 불러오기
     */
    @Transactional
    suspend fun getMyIngredient() : List<IngredientResponseDto> {
        val result = ingredientRepository.findAllIngredients()
        return result.map { it.toResponse() }
    }
    /**
     * 재료 추가하기 및 업데이트
     */
    @Transactional
    suspend fun saveIngredient(ingredientRequestDto: IngredientRequestDto) : IngredientResponseDto {
        val result = ingredientRepository.save(ingredientRequestDto.toEntity())
        return result.toResponse()
    }

    /**
     * 재료 삭제하기
     */
    @Transactional
    suspend fun deleteIngredient(id: Long) {
        ingredientRepository.deleteById(id)
    }

    /**
     * 즐겨찾기 재료 불러오기
     */
    @Transactional
    suspend fun getMyFavoriteIngredients() : List<FavoriteResponseDto> {
        val result = favoriteRepository.findAllFavorites()
        return result.map { it.toResponse() }
    }

    /**
     * 즐겨찾기 재료 추가하기
     */
    @Transactional
    suspend fun createNewFavorite(favoriteRequestDto: FavoriteRequestDto) : FavoriteResponseDto {
        val category = favoriteRepository.findByCategory(favoriteRequestDto.category)
        if (category != null) {
            throw RuntimeException("이미 추가된 재료입니다!")
        }
        val result = favoriteRepository.save(favoriteRequestDto.toEntity())
        return result.toResponse()
    }

    /**
     * 즐겨찾기 재료 삭제하기
     */
    @Transactional
    suspend fun deleteFavorite(favoriteRequestDto: FavoriteRequestDto) : String {
        favoriteRepository.deleteByCategory(favoriteRequestDto.category)
        return "삭제되었습니다!"
    }
}