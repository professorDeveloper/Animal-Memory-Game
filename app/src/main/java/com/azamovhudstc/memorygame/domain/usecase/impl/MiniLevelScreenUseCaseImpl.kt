package com.azamovhudstc.memorygame.domain.usecase.impl

import com.azamovhudstc.memorygame.data.room.entity.GameEntity
import kotlinx.coroutines.flow.Flow
import com.azamovhudstc.memorygame.domain.repository.AppRepository
import com.azamovhudstc.memorygame.domain.usecase.MiniLevelScreenUseCase
import javax.inject.Inject

class MiniLevelScreenUseCaseImpl @Inject constructor(
    private val repository: AppRepository
) : MiniLevelScreenUseCase {

    override fun getAllEasyLevel(): Flow<List<GameEntity>> = repository.getAllEasyLevel()

    override fun getAllMediumLevel(): Flow<List<GameEntity>> = repository.getAllMediumLevel()

    override fun getAllHardLevel(): Flow<List<GameEntity>> = repository.getAllHardLevel()
}