package com.example.weareloversbackup.appPreferences

import kotlinx.coroutines.flow.Flow

interface IAppPreferencesDataStore {
    fun getThemeColorFlow(): Flow<Int>
    fun setThemeColor(color: Int)
    fun getFontColorFlow(): Flow<Int>
    fun setFontColor(color: Int)
    fun getFontFamilyFlow(): Flow<Int>
    fun setFontFamily(fontFamily: Int)
}