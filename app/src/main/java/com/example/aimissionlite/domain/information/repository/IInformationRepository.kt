package com.example.aimissionlite.domain.information.repository

interface IInformationRepository {
    fun getInformation():Map<String,String>
}