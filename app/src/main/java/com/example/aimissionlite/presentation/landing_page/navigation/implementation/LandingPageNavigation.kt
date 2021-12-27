package com.example.aimissionlite.presentation.landing_page.navigation.implementation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.aimissionlite.R
import com.example.aimissionlite.data.BUNDLE_ID_GOAL
import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.presentation.landing_page.navigation.ILandingPageNavigation

class LandingPageNavigation(
    private val navController: NavController
) : ILandingPageNavigation {
    override fun navigateLandingPageToAddGoal() {
        navController.navigate(R.id.action_LandingPageFragment_to_AddGoalFragment)
    }

    override fun navigateLandingPageToAddGoal(goal: Goal) {
        val bundle = bundleOf(BUNDLE_ID_GOAL to goal.id)
        navController.navigate(R.id.action_LandingPageFragment_to_AddGoalFragment, bundle)
    }

    override fun navigateLandingPageToInfo() {
        navController.navigate(R.id.action_LandingPageFragment_to_InfoFragment)
    }

    override fun navigateLandingPageToSettings() {
        navController.navigate(R.id.action_LandingPageFragment_to_SettingsFragment)
    }
}