package com.azamovhudstc.memorygame.viewmodel.impl

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azamovhudstc.memorygame.data.room.entity.GameEntity
import com.azamovhudstc.memorygame.domain.usecase.MiniLevelScreenUseCase
import com.azamovhudstc.memorygame.navigator.Navigator
import com.azamovhudstc.memorygame.screens.GameScreen
import com.azamovhudstc.memorygame.screens.MainScreenDirections
import com.azamovhudstc.memorygame.screens.MinLevelScreenDirections
import com.azamovhudstc.memorygame.viewmodel.MiniScreenViewModel
import com.azamovhudstc.utils.eventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MiniScreenViewModelImpl @Inject constructor(
    private val navigator: Navigator,
    private val useCase: MiniLevelScreenUseCase
) : MiniScreenViewModel, ViewModel() {

    override val easyLevelsList = eventFlow<List<GameEntity>>()

    override val mediumLevelsList = eventFlow<List<GameEntity>>()

    override val hardLevelsList = eventFlow<List<GameEntity>>()

    override fun back() {
        viewModelScope.launch {
            navigator.back()
        }
    }

    override fun openGameScreen(gameEntity: GameEntity) {
        viewModelScope.launch {
            navigator.navigateTo(
                MinLevelScreenDirections.actionMiniLevelScreenToGameScreen(
                    gameEntity
                )
            )
        }
    }

    override fun generateEasy() {
        viewModelScope.launch {
            useCase.getAllEasyLevel().collectLatest {
                easyLevelsList.emit(it)
            }
        }

    }

    override fun generateMedium() {
        viewModelScope.launch {
            useCase.getAllMediumLevel().collectLatest {
                mediumLevelsList.emit(it)
            }
        }
    }

    override fun generateHard() {
        viewModelScope.launch {
            useCase.getAllHardLevel().collectLatest {
                hardLevelsList.emit(it)
            }
        }
    }
}