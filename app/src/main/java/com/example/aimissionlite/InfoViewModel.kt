package com.example.aimissionlite

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InfoViewModel(
    resources: Resources,
    val view: InfoFragment
) : ViewModel() {

    init {
        view.setAuthor(resources.getString(R.string.fragment_info_author_value_text))
        view.setVersionName(resources.getString(R.string.fragment_info_app_version_value_text))
    }

    class InfoViewModelFactory(
        private val resources: Resources,
        private val view: InfoFragment
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InfoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InfoViewModel(resources, view) as T
            }
            throw IllegalArgumentException("Unknown viewmodel class")
        }
    }
}