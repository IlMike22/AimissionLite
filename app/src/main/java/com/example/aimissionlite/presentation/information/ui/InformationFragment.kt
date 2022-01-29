package com.example.aimissionlite.presentation.information.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.aimissionlite.BR
import com.example.aimissionlite.R
import com.example.aimissionlite.core.Resource
import com.example.aimissionlite.data.AUTHOR_NAME_LABEL
import com.example.aimissionlite.data.VERSION_NAME_LABEL
import com.example.aimissionlite.databinding.FragmentInfoBinding
import com.example.aimissionlite.presentation.information.InformationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_info.*

@AndroidEntryPoint
class InformationFragment : Fragment() {
    var informationFragment: InformationFragment? = null

    private val TAG = "InformationFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: InformationViewModel by viewModels()

        viewModel.information.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { information ->
                        try {
                            fragment_info_author_label_text.text = AUTHOR_NAME_LABEL
                            fragment_info_author_value_text.text =
                                information.get(AUTHOR_NAME_LABEL)
                            fragment_info_app_version_label_text.text = VERSION_NAME_LABEL
                            fragment_info_app_version_value_text.text =
                                information.get(VERSION_NAME_LABEL)
                        } catch (exception: Exception) {
                            Log.e(TAG, "Cannot set information: ${exception.message}")
                        }
                    }
                }
                else -> {
                    Log.e(TAG, "Unknown error. Fragment element settings not succeeded.")
                }
            }
        })

        val binding: FragmentInfoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false)
        binding.lifecycleOwner = this

        binding.setVariable(BR.viewModel, viewModel) // binding the view model and execute this
        binding.executePendingBindings()

        informationFragment = this

        return binding.root

    }
}