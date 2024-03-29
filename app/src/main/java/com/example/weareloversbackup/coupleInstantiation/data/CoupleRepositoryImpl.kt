package com.example.weareloversbackup.coupleInstantiation.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoupleRepositoryImpl @Inject constructor(
    private val coupleDataStore: ICoupleDataStore
) : ICoupleRepository {
    override fun getYourNameFlow(): Flow<String> {
        return coupleDataStore.getYourNameFlow()
    }

    override fun getYourPartnerNameFlow(): Flow<String> {
        return coupleDataStore.getYourPartnerNameFlow()
    }

    override fun getCoupleDateFlow(): Flow<Long> {
        return coupleDataStore.getCoupleDateFlow()
    }

    override fun getCoupleImageFlow(): Flow<String> {
        return coupleDataStore.getCoupleImageFlow()
    }

    override fun getYourImageFlow(): Flow<String> {
        return coupleDataStore.getYourImageFlow()
    }

    override fun getYourPartnerImageFlow(): Flow<String> {
        return coupleDataStore.getYourPartnerImageFlow()
    }

    override fun setYourName(name: String) {
        return coupleDataStore.setYourName(name)
    }

    override fun setYourPartnerName(name: String) {
        return coupleDataStore.setYourPartnerName(name)
    }

    override fun setCoupleDate(date: Long) {
        return coupleDataStore.setCoupleDate(date)
    }

    override fun setCoupleImage(image: String) {
        return coupleDataStore.setCoupleImage(image)
    }

    override fun setYourImage(image: String) {
        return coupleDataStore.setYourImage(image)
    }

    override fun setYourPartnerImage(image: String) {
        return coupleDataStore.setYourPartnerImage(image)
    }
}