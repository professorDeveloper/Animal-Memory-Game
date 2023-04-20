package com.azamovhudstc.memorygame.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.azamovhudstc.memorygame.data.room.entity.GameEntity

@Dao
interface GameDao : BaseDao<GameEntity> {

    @Query("Select * From game Where game.level = 1")
    fun getAllEasyLevel(): Flow<List<GameEntity>>

    @Query("Select * From game ")
    fun getAllLevel(): Flow<List<GameEntity>>

    @Query("Select * From game Where game.level = 2")
    fun getAllMediumLevel(): Flow<List<GameEntity>>

    @Query("Select * From game Where game.level = 3")
    fun getAllHardLevel(): Flow<List<GameEntity>>

    @Query("Select * From game Where game.level = :level  and game.number = :number")
    fun getByNumber(level: Int, number: Int): Flow<GameEntity>
}