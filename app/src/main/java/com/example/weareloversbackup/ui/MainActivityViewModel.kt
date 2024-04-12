package com.example.weareloversbackup.ui

import androidx.lifecycle.ViewModel
import com.example.weareloversbackup.coupleInstantiation.data.ICoupleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val coupleRepository: ICoupleRepository
) : ViewModel() {
    val backgroundImageFlow = coupleRepository.getCoupleImageFlow()
}