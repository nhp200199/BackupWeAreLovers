package com.example.weareloversbackup.di

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.weareloversbackup.data.constant.SHARE_PREF_USER_INFO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun prefUserInfo(application: Application): SharedPreferences = application.getSharedPreferences(
        SHARE_PREF_USER_INFO, AppCompatActivity.MODE_PRIVATE)
}