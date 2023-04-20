package com.azamovhudstc.memorygame.viewmodel.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azamovhudstc.memorygame.domain.usecase.MenuScreenUseCase
import com.azamovhudstc.memorygame.navigator.Navigator
import com.azamovhudstc.memorygame.screens.MainScreenDirections
import com.azamovhudstc.memorygame.viewmodel.MenuScreenViewModel
import com.azamovhudstc.utils.eventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MenuScreenViewModelImpl @Inject constructor(
    private val useCase: MenuScreenUseCase,
    private val navigator: Navigator
) : MenuScreenViewModel, ViewModel() {

    override val levelFlow = eventFlow<String>()

    init {
        viewModelScope.launch {
            useCase.getLevel().collectLatest {
                levelFlow.emit(it)
            }
        }
    }

    override fun onClickPlay(level: String) {
        viewModelScope.launch {
            navigator.navigateTo(MainScreenDirections.actionMenuScreenToMiniLevelScreen(level))
        }
    }

    override suspend fun setLevel(level: String) = useCase.setLevel(level)

    override fun onClickNext(level: String) {
        viewModelScope.launch {
            when (level) {
                "4x4\neasy" -> {
                    levelFlow.emit(
                        "6x6\n" +
                                "medium"
                    )
                }
                "6x6\nmedium" -> {
                    levelFlow.emit(
                        "6x8\n" +
                                "hard"
                    )
                }
                "6x8\nhard" -> {
                    levelFlow.emit(
                        "4x4\n" +
                                "easy"
                    )
                }
            }
        }
    }

    override fun onClickPrev(level: String) {
        viewModelScope.launch {
            viewModelScope.launch {
                when (level) {
                    "4x4\neasy" -> {
                        levelFlow.emit(
                            "6x8\n" +
                                    "hard"
                        )
                    }
                    "6x6\nmedium" -> {
                        levelFlow.emit(
                            "4x4\n" +
                                    "easy"
                        )
                    }
                    "6x8\nhard" -> {
                        levelFlow.emit(
                            "6x6\n" +
                                    "medium"
                        )
                    }
                }
            }
        }
    }

    override fun onClickSetting() {

    }

    override fun onClickInfo() {

    }


}