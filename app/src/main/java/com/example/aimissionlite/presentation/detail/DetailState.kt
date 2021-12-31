package com.example.aimissionlite.presentation.detail

sealed class DetailState<T>(val data: T? = null) {
    class ShowEditGoal<T>(goal: T? = null) : DetailState<T>(data = goal)
}
