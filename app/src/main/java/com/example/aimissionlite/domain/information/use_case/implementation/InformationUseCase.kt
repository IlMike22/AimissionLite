package com.example.aimissionlite.domain.information.use_case.implementation

import com.example.aimissionlite.domain.information.repository.IInformationRepository
import com.example.aimissionlite.domain.information.use_case.IInformationUseCase

class InformationUseCase(
    val repository: IInformationRepository
) : IInformationUseCase {
    override fun getInformation(): Map<String, String> = repository.getInformation()
}