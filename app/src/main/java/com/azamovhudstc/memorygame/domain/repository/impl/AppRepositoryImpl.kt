package com.azamovhudstc.memorygame.domain.repository.impl

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import androidx.core.content.ContentProviderCompat.requireContext
import com.azamovhudstc.memorygame.data.images.Database
import com.azamovhudstc.memorygame.data.model.GameModel
import com.azamovhudstc.memorygame.data.model.Level
import com.azamovhudstc.memorygame.data.room.dao.GameDao
import com.azamovhudstc.memorygame.data.room.entity.GameEntity
import com.azamovhudstc.memorygame.data.shp.MySharedPreference
import com.azamovhudstc.memorygame.databinding.DialogExitBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import com.azamovhudstc.memorygame.domain.repository.AppRepository
import javax.inject.Inject


class AppRepositoryImpl @Inject constructor(
    private val dao: GameDao
) : AppRepository {

    private val gson = Gson()
    private val shp = MySharedPreference.getInstance()

    override fun getAllEasyLevel(): Flow<List<GameEntity>> = flow<List<GameEntity>> {
        if (!shp.firstEasy) {
            val level = Level.EASY
            val list = ArrayList<GameEntity>()

            val count = level.x * level.y
            val data = Database.images
            var s = ""
            for (i in 0..51) {

                data.shuffle()
                val a = data.subList(0, count / 2)

                val result = ArrayList<GameModel>()
                result.addAll(a)
                result.addAll(a)
                result.shuffle()

                val type = object : TypeToken<List<GameModel>>() {}.type
                s = gson.toJson(result, type)

                list.add(GameEntity(0, s, i + 1, 0, 0, 1, false))
            }

            dao.insert(list)
            shp.firstEasy = true
        }
        dao.getAllEasyLevel().map {
            emit(it)
        }.collect()

    }.flowOn(Dispatchers.IO)

    override fun getAllMediumLevel(): Flow<List<GameEntity>> = flow<List<GameEntity>> {
        if (!shp.firstMedium) {
            val level = Level.MEDIUM
            val list = ArrayList<GameEntity>()

            val count = level.x * level.y
            val data = Database.images
            var s = ""
            for (i in 0..51) {
                data.shuffle()
                val a = data.subList(0, count / 2)

                val result = ArrayList<GameModel>()
                result.addAll(a)
                result.addAll(a)
                result.shuffle()

                val type = object : TypeToken<List<GameModel>>() {}.type
                s = gson.toJson(result, type)
                list.add(GameEntity(0, s, i + 1, 0, 0, 2, false))
            }

            dao.insert(list)
            shp.firstMedium = true
        }
        dao.getAllMediumLevel().map {
            emit(it)
        }.collect()
    }.flowOn(Dispatchers.IO)

    override fun getAllHardLevel(): Flow<List<GameEntity>> = flow<List<GameEntity>> {
        if (!shp.firstHard) {
            val level = Level.HARD
            val list = ArrayList<GameEntity>()

            val count = level.x * level.y
            val data = Database.images
            var s = ""
            for (i in 0..51) {
                data.shuffle()
                val a = data.subList(0, count / 2)

                val result = ArrayList<GameModel>()
                result.addAll(a)
                result.addAll(a)
                result.shuffle()

                val type = object : TypeToken<List<GameModel>>() {}.type
                s = gson.toJson(result, type)
                list.add(GameEntity(0, s, i + 1, 0, 0, 3, false))

            }
            dao.insert(list)
            shp.firstHard = true
        }
        dao.getAllHardLevel().map {
            emit(it)
        }.collect()
    }.flowOn(Dispatchers.IO)

    override fun update(entity: GameEntity) = dao.update(entity)

    override fun getLevel(): Flow<String> = flow {
        emit(shp.level)
    }

    override suspend fun openNextLevel(level: Int, number: Int) {
        dao.getAllLevel().map { it ->
            it.map {
                if (it.level == level && it.number == number) {
                    it.state = true
                    dao.update(it)
                }
            }
        }.collect()
    }

    override suspend fun setLevel(level: String) {
        shp.level = level
    }

    override fun getByNumber(level: Int, number: Int): Flow<List<GameModel>> = flow {

        val type = object : TypeToken<List<GameModel>>() {}.type
        dao.getByNumber(level, number).map {
            val gameModel = gson.fromJson<List<GameModel>>(it.imageList, type)
            emit(gameModel)
        }.collect()

    }.flowOn(Dispatchers.IO)


}