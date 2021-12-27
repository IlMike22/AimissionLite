package com.example.aimissionlite.di

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.aimissionlite.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

//@Module
//@InstallIn(ActivityComponent::class)
//object ActivityModule {
//    @Provides
//    fun provideNavController(activity: Activity): NavController =
//        activity.findNavController(R.id.nav_host_fragment)
//}