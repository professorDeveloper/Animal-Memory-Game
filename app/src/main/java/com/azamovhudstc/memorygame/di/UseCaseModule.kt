package com.azamovhudstc.memorygame.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import com.azamovhudstc.memorygame.domain.usecase.GameUseCase
import com.azamovhudstc.memorygame.domain.usecase.MenuScreenUseCase
import com.azamovhudstc.memorygame.domain.usecase.MiniLevelScreenUseCase
import com.azamovhudstc.memorygame.domain.usecase.impl.GameUseCaseImpl
import com.azamovhudstc.memorygame.domain.usecase.impl.MenuScreenUseCaseImpl
import com.azamovhudstc.memorygame.domain.usecase.impl.MiniLevelScreenUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {
    @Binds
    fun bindGameUseCase(impl: GameUseCaseImpl): GameUseCase

    @Binds
    fun bindMenuUseCase(impl: MenuScreenUseCaseImpl): MenuScreenUseCase

    @Binds
    fun bindMiniLevelUseCase(impl: MiniLevelScreenUseCaseImpl): MiniLevelScreenUseCase
}