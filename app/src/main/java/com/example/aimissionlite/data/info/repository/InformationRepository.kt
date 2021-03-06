package com.example.aimissionlite.data.info.repository

import com.example.aimissionlite.data.AUTHOR_NAME_LABEL
import com.example.aimissionlite.data.AUTHOR_NAME_VALUE
import com.example.aimissionlite.data.VERSION_NAME_LABEL
import com.example.aimissionlite.data.VERSION_NAME_VALUE
import com.example.aimissionlite.domain.information.repository.IInformationRepository

class InformationRepository() : IInformationRepository {
    override fun getInformation(): Map<String, String> {
        val information = mutableMapOf<String, String>()
        information[AUTHOR_NAME_LABEL] = AUTHOR_NAME_VALUE
        information[VERSION_NAME_LABEL] = VERSION_NAME_VALUE
        return information
    }
}