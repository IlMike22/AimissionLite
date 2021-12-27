package com.example.aimissionlite.presentation.landing_page.navigation

import com.example.aimissionlite.models.domain.Goal

interface ILandingPageNavigation {
    fun navigateLandingPageToAddGoal()
    fun navigateLandingPageToInfo()
    fun navigateLandingPageToSettings()
    fun navigateLandingPageToAddGoal(goal: Goal)
}