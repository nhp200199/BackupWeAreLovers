package com.example.weareloversbackup.coupleInstantiation.data

import kotlinx.coroutines.flow.Flow

interface ICoupleRepository {
    fun getYourNameFlow(): Flow<String>
    fun getYourPartnerNameFlow(): Flow<String>
    fun getCoupleDateFlow(): Flow<Long>
    fun getCoupleImageFlow(): Flow<String>
    fun getYourImageFlow(): Flow<String>
    fun getYourPartnerImageFlow(): Flow<String>
    fun setYourName(name: String)
    fun setYourPartnerName(name: String)
    fun setCoupleDate(date: Long)
    fun setCoupleImage(image: String)
    fun setYourImage(image: String)
    fun setYourPartnerImage(image: String)
}