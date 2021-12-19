package com.example.aimissionlite.presentation.info.ui

import androidx.lifecycle.MutableLiveData

interface IInfoFragment {
    val versionName: MutableLiveData<String>
    val author: MutableLiveData<String>

    fun setAuthor(name:String)

    fun setVersionName(version:String)
}