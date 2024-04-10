package com.example.weareloversbackup.di

import com.example.weareloversbackup.utils.helper.IPermissionHelper
import com.example.weareloversbackup.utils.helper.PermissionHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class ActivityModule {
    @Binds
    abstract fun permissionHelper(permissionHelperImpl: PermissionHelperImpl): IPermissionHelper

}