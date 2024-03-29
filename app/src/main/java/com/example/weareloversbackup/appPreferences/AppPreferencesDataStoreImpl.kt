package com.example.weareloversbackup.appPreferences

import android.content.SharedPreferences
import com.example.weareloversbackup.data.constant.PREF_FONT_COLOR
import com.example.weareloversbackup.data.constant.PREF_FONT_FAMILY
import com.example.weareloversbackup.data.constant.PREF_THEME_COLOR
import com.phucnguyen.lovereminder.di.PrefPreferenceSetting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AppPreferencesDataStoreImpl @Inject constructor(
    @PrefPreferenceSetting private val sharedPreferences: SharedPreferences
) : IAppPreferencesDataStore {
    private val themeColorFlow: MutableStateFlow<Int>
    private val fontColorFlow: MutableStateFlow<Int>
    private val fontFamilyFlow: MutableStateFlow<Int>

    init {
        val themColor = sharedPreferences.getInt(PREF_THEME_COLOR, 0)
        val fontColor = sharedPreferences.getInt(PREF_FONT_COLOR, 0)
        val fontFamily = sharedPreferences.getInt(PREF_FONT_FAMILY, 0)
        themeColorFlow = MutableStateFlow(themColor)
        fontColorFlow = MutableStateFlow(fontColor)
        fontFamilyFlow = MutableStateFlow(fontFamily)
    }

    override fun getThemeColorFlow(): Flow<Int> {
        return themeColorFlow.asStateFlow()
    }

    override fun setThemeColor(color: Int) {
        themeColorFlow.value = color
    }

    override fun getFontColorFlow(): Flow<Int> {
        return fontColorFlow.asStateFlow()
    }

    override fun setFontColor(color: Int) {
        fontColorFlow.value = color
    }

    override fun getFontFamilyFlow(): Flow<Int> {
        return fontFamilyFlow.asStateFlow()
    }

    override fun setFontFamily(fontFamily: Int) {
        fontFamilyFlow.value = fontFamily
    }
}