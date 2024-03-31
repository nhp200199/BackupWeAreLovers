package com.example.weareloversbackup.main.home.domain

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.weareloversbackup.coupleInstantiation.data.ICoupleRepository
import com.example.weareloversbackup.main.home.ui.UserInfoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(private val coupleRepository: ICoupleRepository) : ViewModel() {
    val userInfoUiStateFlow: Flow<UserInfoUiState> = combine(coupleRepository.getYourNameFlow(),
        coupleRepository.getYourImageFlow(),
        coupleRepository.getYourPartnerNameFlow(),
        coupleRepository.getYourPartnerImageFlow(),
        coupleRepository.getCoupleDateFlow()
    ) {
            yourName, yourImage, yourFrName, yourFrImage, coupleDate ->
        val yourImageUri = Uri.parse(yourImage)
        val yourFrImageUri = Uri.parse(yourFrImage)
        val coupleDayCounts = getCoupleDaysCount(coupleDate)
        UserInfoUiState(yourName, yourFrName, yourImageUri, yourFrImageUri, coupleDayCounts.toString())
    }

    private fun getCoupleDaysCount(coupleDate: Long): Long {
        val current = Calendar.getInstance().timeInMillis
        val diffTimestamps = current - coupleDate
        return TimeUnit.DAYS.convert(diffTimestamps, TimeUnit.MILLISECONDS)
    }

    fun setYourName(newName: String) {
        coupleRepository.setYourName(newName)
    }

    fun setYourPartnerName(newName: String) {
        coupleRepository.setYourPartnerName(newName)
    }

    fun setCoupleDate(date: Long) {
        coupleRepository.setCoupleDate(date)
    }

    fun setYourImage(newImage: String) {
        coupleRepository.setYourImage(newImage)
    }

    fun setYourPartnerImage(newImage: String) {
        coupleRepository.setYourPartnerImage(newImage)
    }
}