package com.example.aimissionlite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.aimissionlite.databinding.FragmentDetailBinding
import com.example.aimissionlite.databinding.FragmentInfoBinding
import kotlinx.android.synthetic.main.activity_main.*

class InfoFragment : IInfoFragment, Fragment() {
    var infoFragment: InfoFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentInfoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false)
        binding.lifecycleOwner = this

        binding.setVariable(BR.viewModel, viewModel) // binding the view model and execute this
        binding.executePendingBindings()

        infoFragment = this

        return binding.root

    }

    override val versionName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    override val author: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    override fun setAuthor(name: String) {
        author.value = name
    }

    override fun setVersionName(version: String) {
        versionName.value = version
    }

    private val viewModel: InfoViewModel by viewModels {
        InfoViewModel.InfoViewModelFactory(
            resources = resources,
            view = this
        )
    }
}