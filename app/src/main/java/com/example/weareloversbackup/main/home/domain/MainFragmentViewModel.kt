package com.example.weareloversbackup.main.home.domain

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weareloversbackup.coupleInstantiation.data.ICoupleRepository
import com.example.weareloversbackup.main.home.ui.DEFAULT_USER_INFO_UI_STATE
import com.example.weareloversbackup.main.home.ui.UserInfoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(private val coupleRepository: ICoupleRepository) : ViewModel() {
    private val _userInfoUiStateFlow: StateFlow<UserInfoUiState> = combine(coupleRepository.getYourNameFlow(),
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
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        DEFAULT_USER_INFO_UI_STATE
    )
    val userInfoUiStateFlow: Flow<UserInfoUiState> = _userInfoUiStateFlow

    private val _isEditingCoupleDate = MutableStateFlow<Boolean>(false)
    val isEditingCoupleDataFlow: Flow<Boolean> = _isEditingCoupleDate.drop(1)

    private fun getCoupleDaysCount(coupleDate: Long): Long {
        val current = Calendar.getInstance().timeInMillis
        val diffTimestamps = current - coupleDate
        return TimeUnit.DAYS.convert(diffTimestamps, TimeUnit.MILLISECONDS)
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

    fun cancelEditCoupleData() {
        setIsEditingCoupleDate(false)
    }

    fun saveCoupleData() {
        coupleRepository.saveYourName(_userInfoUiStateFlow.value.yourName)
        coupleRepository.saveYourPartnerName(_userInfoUiStateFlow.value.yourFrName)
        coupleRepository.saveCoupleDate()
    }

    fun setIsEditingCoupleDate(isEditing: Boolean) {
        _isEditingCoupleDate.value = isEditing
    }

    fun isEditingCoupleData(): Boolean {
        return _isEditingCoupleDate.value
    }
}