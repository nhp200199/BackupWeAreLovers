package com.example.weareloversbackup.coupleInstantiation.domain

import androidx.lifecycle.ViewModel
import com.example.weareloversbackup.coupleInstantiation.data.ICoupleRepository
import com.example.weareloversbackup.coupleInstantiation.ui.CoupleInstantiationFormUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CoupleInstantiationViewModel @Inject constructor(
    private val coupleRepository: ICoupleRepository
) : ViewModel() {
    private val _yourNameInputStateFlow = MutableStateFlow<String?>(null)
    private val _yourPartnerNameInputStateFlow = MutableStateFlow<String?>(null)
    private val _coupleDateStateFlow = MutableStateFlow<String?>(null)
    val coupleDateStateFlow = _coupleDateStateFlow.filterNotNull()

    private val _yourNameInputErrorStateFlow = _yourNameInputStateFlow.map {
        val errors = mutableListOf<String>()
        if (it == null) {
            errors.add("Init")
        } else {
            if (it.isBlank()) {
                errors.add("Your name is required")
            }
        }
        errors
    }
    private val _yourPartnerNameInputErrorStateFlow = _yourPartnerNameInputStateFlow.map {
        val errors = mutableListOf<String>()
        if (it == null) {
            errors.add("Init")
        } else {
            if (it.isBlank()) {
                errors.add("Your partner's name is required")
            }
        }
        errors
    }

    private val _coupleDateErrorStateFlow = _coupleDateStateFlow.map {
        val errors = mutableListOf<String>()
        if (it == null) {
            errors.add("Init")
        }
        errors
    }

    val coupleInstantiationUIState = combine(_yourNameInputErrorStateFlow,
            _yourPartnerNameInputErrorStateFlow, _coupleDateErrorStateFlow) { yourNameErrors, yourPartnerErrors, coupleDateErrors ->
        return@combine CoupleInstantiationFormUIState(
            yourNameErrors,
            yourPartnerErrors,
            coupleDateErrors,
            isFormValid = yourNameErrors.isEmpty() && yourPartnerErrors.isEmpty() && coupleDateErrors.isEmpty()
        )
    }

    fun setCoupleDate(date: String) {
        _coupleDateStateFlow.value = date
    }

    fun setYourNameInput(input: String) {
        _yourNameInputStateFlow.value = input
    }

    fun setYourPartnerNameInput(input: String) {
        _yourPartnerNameInputStateFlow.value = input
    }

    fun saveYourName(yourName: String) {
        coupleRepository.setYourName(yourName)
    }

    fun saveYourPartnerName(yourPartnerName: String) {
        coupleRepository.setYourPartnerName(yourPartnerName)
    }

    fun saveCoupleDate(timestamps: Long) {
        coupleRepository.setCoupleDate(timestamps)
    }
}