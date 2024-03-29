package com.example.weareloversbackup.di

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.weareloversbackup.appPreferences.AppPreferencesDataStoreImpl
import com.example.weareloversbackup.appPreferences.AppPreferencesRepositoryImpl
import com.example.weareloversbackup.appPreferences.IAppPreferencesDataStore
import com.example.weareloversbackup.appPreferences.IAppPreferencesRepository
import com.example.weareloversbackup.coupleInstantiation.data.CoupleDataStoreImpl
import com.example.weareloversbackup.coupleInstantiation.data.CoupleRepositoryImpl
import com.example.weareloversbackup.coupleInstantiation.data.ICoupleDataStore
import com.example.weareloversbackup.coupleInstantiation.data.ICoupleRepository
import com.example.weareloversbackup.data.constant.SHARE_PREF_USER_INFO
import com.example.weareloversbackup.data.constant.SHARE_PREF_USER_PREFERENCE
import com.phucnguyen.lovereminder.di.PrefPreferenceSetting
import com.phucnguyen.lovereminder.di.PrefUserInfo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Singleton
    @Binds
    abstract fun coupleDataStore(coupleDataStoreImpl: CoupleDataStoreImpl): ICoupleDataStore

    @Singleton
    @Binds
    abstract fun appPreferencesDataStore(
        appPreferencesDataStoreImpl: AppPreferencesDataStoreImpl): IAppPreferencesDataStore

    @Singleton
    @Binds
    abstract fun appPreferencesRepository(
        appPreferencesRepositoryImpl: AppPreferencesRepositoryImpl): IAppPreferencesRepository

    @Singleton
    @Binds
    abstract fun coupleRepository(
        coupleRepositoryImpl: CoupleRepositoryImpl): ICoupleRepository
    companion object {
        @Singleton
        @Provides
        @PrefUserInfo
        fun prefUserInfo(application: Application): SharedPreferences = application.getSharedPreferences(
            SHARE_PREF_USER_INFO, AppCompatActivity.MODE_PRIVATE)

        @Singleton
        @Provides
        @PrefPreferenceSetting
        fun prePreferencesSetting(application: Application): SharedPreferences = application.getSharedPreferences(
            SHARE_PREF_USER_PREFERENCE, AppCompatActivity.MODE_PRIVATE)
    }
}