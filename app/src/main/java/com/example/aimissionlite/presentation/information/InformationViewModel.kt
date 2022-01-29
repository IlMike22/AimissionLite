package com.example.aimissionlite.presentation.information

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aimissionlite.core.Resource
import com.example.aimissionlite.domain.information.use_case.IInformationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(
    private val useCase: IInformationUseCase
) : ViewModel() {

    val information = MutableLiveData<Resource<Map<String, String>>>()

    init {
        setInformation()
    }

    private fun setInformation() {
        information.postValue(Resource.Loading())
        information.postValue(
            Resource.Success(
                data = useCase.getInformation()
            )
        )
    }
}