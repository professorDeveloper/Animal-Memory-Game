package com.azamovhudstc.memorygame.di

import com.azamovhudstc.memorygame.navigator.NavigationDispatcher
import com.azamovhudstc.memorygame.navigator.NavigationHandler
import com.azamovhudstc.memorygame.navigator.Navigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun navigator(dispatcher: NavigationDispatcher): Navigator

    @Binds
    fun navigatorHandler(dispatcher: NavigationDispatcher): NavigationHandler
}