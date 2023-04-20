package com.azamovhudstc.memorygame.domain.usecase

import com.azamovhudstc.memorygame.data.room.entity.GameEntity
import kotlinx.coroutines.flow.Flow

interface MiniLevelScreenUseCase {

    fun getAllEasyLevel(): Flow<List<GameEntity>>

    fun getAllMediumLevel(): Flow<List<GameEntity>>

    fun getAllHardLevel(): Flow<List<GameEntity>>
}