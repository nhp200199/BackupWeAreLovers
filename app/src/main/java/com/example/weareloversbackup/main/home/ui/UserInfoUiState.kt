package com.example.weareloversbackup.main.home.ui

import android.net.Uri

data class UserInfoUiState(
    val yourName: String,
    val yourFrName: String,
    val yourImage: Uri,
    val yourFrImage: Uri,
    val coupleDate: String
)

val DEFAULT_USER_INFO_UI_STATE = UserInfoUiState(
    "",
    "",
    Uri.EMPTY,
    Uri.EMPTY,
    ""
)