package com.azamovhudstc.memorygame.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.azamovhudstc.memorygame.data.model.GameModel
import com.azamovhudstc.memorygame.data.room.entity.GameEntity
import kotlinx.coroutines.flow.Flow


interface GameScreenViewModel {

    val gameModelLiveData: Flow<List<GameModel>>

    fun back()

    fun getByNumber(level: Int, number: Int)

    fun saveResult(entity: GameEntity)

    fun btnClicked(state: Boolean, position: Int)

    fun openNextLevel(level: Int, number: Int)
}