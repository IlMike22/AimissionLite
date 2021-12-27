package com.example.aimissionlite.domain.info.use_case

import com.example.aimissionlite.domain.info.repository.IInfoRepository

class GetInformationUseCase(
    val repository: IInfoRepository
) {
    operator fun invoke(): Map<String, String> = repository.getInfo()
}