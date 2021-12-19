package com.example.aimissionlite.domain.info.use_case

import com.example.aimissionlite.domain.info.repository.InfoRepository

class GetInformationUseCase() {
    private val repository = InfoRepository()

    operator fun invoke(): Map<String, String> = repository.getInfo()
}