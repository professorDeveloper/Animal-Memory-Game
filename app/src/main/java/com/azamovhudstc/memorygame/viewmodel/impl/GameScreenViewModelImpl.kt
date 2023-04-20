package com.azamovhudstc.memorygame.viewmodel.impl

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azamovhudstc.memorygame.data.model.GameModel
import com.azamovhudstc.memorygame.data.room.entity.GameEntity
import com.azamovhudstc.memorygame.domain.usecase.GameUseCase
import com.azamovhudstc.memorygame.navigator.Navigator
import com.azamovhudstc.memorygame.viewmodel.GameScreenViewModel
import com.azamovhudstc.utils.eventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GameScreenViewModelImpl @Inject constructor(
    private val useCase: GameUseCase,
    private val navigator: Navigator
) :
    GameScreenViewModel, ViewModel() {

    override val gameModelLiveData = eventFlow<List<GameModel>>()
    override fun back() {
        viewModelScope.launch {
            navigator.back()
        }
    }

    override fun getByNumber(level: Int, number: Int) {
        viewModelScope.launch {
            useCase.getByNumber(level, number).collect {
                gameModelLiveData.emit(it)
            }
        }
    }

    override fun saveResult(entity: GameEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.saveResult(entity)
        }
    }

    override fun btnClicked(state: Boolean, position: Int) {

    }

    override fun openNextLevel(level: Int, number: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.openNextLevel(level, number)
        }
    }

}
