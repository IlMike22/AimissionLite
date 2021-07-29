package com.example.aimissionlite

import androidx.lifecycle.MutableLiveData

interface ISettingsFragment {
    val header: MutableLiveData<String>

    fun setHeader(text:String)
}