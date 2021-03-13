package com.example.aimissionlite.data

import com.example.aimissionlite.models.Genre

fun Genre.domainToData():String {
    return when(this) {
        Genre.PARTNERSHIP -> "PARTNERSHIP"
        Genre.BUSINESS -> "BUSINESS"
        Genre.FITTNESS -> "FITTNESS"
        Genre.MONEY -> "MONEY"
        Genre.SOCIALISING -> "SOCIALISING"
        Genre.HEALTH -> "HEALTH"
        Genre.UNKNOWN -> "UNKNOWN"
    }
}