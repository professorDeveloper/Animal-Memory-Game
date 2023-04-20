package com.azamovhudstc.memorygame.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.azamovhudstc.memorygame.data.room.entity.GameEntity
import kotlinx.coroutines.flow.Flow

interface MiniScreenViewModel {

    val easyLevelsList: Flow<List<GameEntity>>
    val mediumLevelsList: Flow<List<GameEntity>>
    val hardLevelsList: Flow<List<GameEntity>>

    fun back()

    fun openGameScreen(gameEntity: GameEntity)

    fun generateEasy()
    fun generateMedium()
    fun generateHard()
}