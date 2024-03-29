package com.example.weareloversbackup.appPreferences

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppPreferencesRepositoryImpl @Inject constructor(
    private val appPreferencesDataStore: IAppPreferencesDataStore) : IAppPreferencesRepository {
    override fun getThemeColorFlow(): Flow<Int> {
        return appPreferencesDataStore.getThemeColorFlow()
    }

    override fun setThemeColor(color: Int) {
        return appPreferencesDataStore.setThemeColor(color)
    }

    override fun getFontColorFlow(): Flow<Int> {
        return appPreferencesDataStore.getFontColorFlow()
    }

    override fun setFontColor(color: Int) {
        appPreferencesDataStore.setFontColor(color)
    }

    override fun getFontFamilyFlow(): Flow<Int> {
        return appPreferencesDataStore.getFontFamilyFlow()
    }

    override fun setFontFamily(fontFamily: Int) {
        appPreferencesDataStore.setFontFamily(fontFamily)
    }
}