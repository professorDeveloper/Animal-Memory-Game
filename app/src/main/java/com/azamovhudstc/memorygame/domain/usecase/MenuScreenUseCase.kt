package com.azamovhudstc.memorygame.domain.usecase

import kotlinx.coroutines.flow.Flow

interface MenuScreenUseCase {

    fun getLevel(): Flow<String>

    suspend fun setLevel(level: String)
}