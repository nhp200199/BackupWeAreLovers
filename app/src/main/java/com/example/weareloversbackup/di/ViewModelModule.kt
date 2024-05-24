package com.example.weareloversbackup.di

import com.example.weareloversbackup.appPreferences.AppPreferencesRepositoryImpl
import com.example.weareloversbackup.appPreferences.IAppPreferencesRepository
import com.example.weareloversbackup.coupleInstantiation.data.CoupleRepositoryImpl
import com.example.weareloversbackup.coupleInstantiation.data.ICoupleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    abstract fun appPreferencesRepository(appPreferencesRepositoryImpl: AppPreferencesRepositoryImpl): IAppPreferencesRepository
}