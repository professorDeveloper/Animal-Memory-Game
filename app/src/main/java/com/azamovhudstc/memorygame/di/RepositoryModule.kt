package com.azamovhudstc.memorygame.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.azamovhudstc.memorygame.domain.repository.AppRepository
import com.azamovhudstc.memorygame.domain.repository.impl.AppRepositoryImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @[Binds Singleton]
    fun bindAppRepository(impl: AppRepositoryImpl): AppRepository

}
