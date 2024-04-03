package com.example.weareloversbackup.coupleInstantiation.data

import android.content.SharedPreferences
import android.util.Log
import com.example.weareloversbackup.data.constant.DEFAULT_IMAGE_PATH
import com.example.weareloversbackup.data.constant.PREF_COUPLE_DATE
import com.example.weareloversbackup.data.constant.PREF_COUPLE_IMAGE
import com.example.weareloversbackup.data.constant.PREF_YOUR_FRIEND_IMAGE
import com.example.weareloversbackup.data.constant.PREF_YOUR_FRIEND_NAME
import com.example.weareloversbackup.data.constant.PREF_YOUR_IMAGE
import com.example.weareloversbackup.data.constant.PREF_YOUR_NAME
import com.phucnguyen.lovereminder.di.PrefUserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class CoupleDataStoreImpl @Inject constructor(
    @PrefUserInfo private val sharedPreferences: SharedPreferences
) : ICoupleDataStore {
    private val yourNameFlow: MutableStateFlow<String>
    private val yourPartnerNameFlow: MutableStateFlow<String>
    private val coupleDateFlow: MutableStateFlow<Long>
    private val coupleImageFlow: MutableStateFlow<String>
    private val yourImageFlow: MutableStateFlow<String>
    private val yourPartnerImageFlow: MutableStateFlow<String>
    init {
        val yourName = sharedPreferences.getString(PREF_YOUR_NAME, "")
        val yourPartnerName = sharedPreferences.getString(PREF_YOUR_FRIEND_NAME, "")
        val coupeDateFlow = sharedPreferences.getLong(PREF_COUPLE_DATE, 0L)
        val yourImage = sharedPreferences.getString(PREF_YOUR_IMAGE, DEFAULT_IMAGE_PATH)
        val yourPartnerImage = sharedPreferences.getString(PREF_YOUR_FRIEND_IMAGE, DEFAULT_IMAGE_PATH)
        val coupleImage = sharedPreferences.getString(PREF_COUPLE_IMAGE, DEFAULT_IMAGE_PATH)

        yourNameFlow = MutableStateFlow(yourName!!)
        yourPartnerNameFlow = MutableStateFlow(yourPartnerName!!)
        coupleDateFlow = MutableStateFlow(coupeDateFlow)
        yourImageFlow = MutableStateFlow(yourImage!!)
        yourPartnerImageFlow = MutableStateFlow(yourPartnerImage!!)
        coupleImageFlow = MutableStateFlow(coupleImage!!)
    }

    override fun getYourNameFlow(): Flow<String> {
        return yourNameFlow.asStateFlow()
    }

    override fun getYourPartnerNameFlow(): Flow<String> {
        return yourPartnerNameFlow.asStateFlow()
    }

    override fun getCoupleDateFlow(): Flow<Long> {
        return coupleDateFlow.asStateFlow()
    }

    override fun getCoupleImageFlow(): Flow<String> {
        return coupleImageFlow.asStateFlow()
    }

    override fun getYourImageFlow(): Flow<String> {
        return yourImageFlow.asStateFlow()
    }

    override fun getYourPartnerImageFlow(): Flow<String> {
        return yourPartnerImageFlow.asStateFlow()
    }

    override fun setYourName(name: String) {
        yourNameFlow.value = name
    }

    override fun setYourPartnerName(name: String) {
        yourPartnerNameFlow.value = name
    }

    override fun setCoupleDate(date: Long) {
        Log.d("CoupleDataStore", "saving date = $date")
        sharedPreferences.edit().putLong(PREF_COUPLE_DATE, date).apply()
        coupleDateFlow.value = date
    }

    override fun setCoupleImage(image: String) {
        sharedPreferences.edit().putString(PREF_COUPLE_IMAGE, image).apply()
        coupleImageFlow.value = image
    }

    override fun setYourImage(image: String) {
        sharedPreferences.edit().putString(PREF_YOUR_IMAGE, image).apply()
        yourImageFlow.value = image
    }

    override fun setYourPartnerImage(image: String) {
        sharedPreferences.edit().putString(PREF_YOUR_FRIEND_IMAGE, image).apply()
        yourPartnerImageFlow.value = image
    }

    override fun saveYourName(name: String) {
        setYourName(name)
        sharedPreferences.edit().putString(PREF_YOUR_NAME, name).apply()
    }

    override fun saveYourPartnerName(name: String) {
        setYourPartnerName(name)
        sharedPreferences.edit().putString(PREF_YOUR_FRIEND_NAME, name).apply()
    }
}