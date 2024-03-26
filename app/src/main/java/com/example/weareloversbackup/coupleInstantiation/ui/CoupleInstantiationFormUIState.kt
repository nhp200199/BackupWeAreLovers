package com.example.weareloversbackup.coupleInstantiation.ui

data class CoupleInstantiationFormUIState(
    val yourNameErrors: List<String> = emptyList(),
    val yourPartnerNameErrors: List<String> = emptyList(),
    val coupleDateErrors: List<String> = emptyList(),
    val isFormValid:Boolean = false
) {
}