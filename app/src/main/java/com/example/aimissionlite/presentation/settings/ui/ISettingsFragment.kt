package com.example.aimissionlite.presentation.settings.ui

import androidx.lifecycle.MutableLiveData

interface ISettingsFragment {
    val header: MutableLiveData<String>

    fun setHeader(text:String)
}