package com.azamovhudstc.memorygame.domain.usecase

import com.azamovhudstc.memorygame.data.model.GameModel
import com.azamovhudstc.memorygame.data.room.entity.GameEntity
import kotlinx.coroutines.flow.Flow


interface GameUseCase {

    fun getByNumber(level: Int, number: Int): Flow<List<GameModel>>

    suspend fun saveResult(entity: GameEntity)

    suspend fun openNextLevel(level: Int, number: Int)
}