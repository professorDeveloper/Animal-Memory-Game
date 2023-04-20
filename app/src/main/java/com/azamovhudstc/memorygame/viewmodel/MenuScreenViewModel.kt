package com.azamovhudstc.memorygame.viewmodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface MenuScreenViewModel {

    val levelFlow: Flow<String>

    fun onClickPlay(level: String)

    suspend fun setLevel(level: String)

    fun onClickNext(level: String)

    fun onClickPrev(level: String)

    fun onClickSetting()

    fun onClickInfo()

}