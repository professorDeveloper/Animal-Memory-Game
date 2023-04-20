package com.azamovhudstc.memorygame.domain.usecase.impl

import com.azamovhudstc.memorygame.data.model.GameModel
import com.azamovhudstc.memorygame.data.room.entity.GameEntity
import kotlinx.coroutines.flow.Flow
import com.azamovhudstc.memorygame.domain.repository.AppRepository
import com.azamovhudstc.memorygame.domain.usecase.GameUseCase
import javax.inject.Inject


class GameUseCaseImpl @Inject constructor(private val repository: AppRepository) : GameUseCase {
    override fun getByNumber(level: Int, number: Int): Flow<List<GameModel>> =
        repository.getByNumber(level, number)

    override suspend fun saveResult(entity: GameEntity) = repository.update(entity)

    override suspend fun openNextLevel(level: Int, number: Int) =
        repository.openNextLevel(level, number)

}