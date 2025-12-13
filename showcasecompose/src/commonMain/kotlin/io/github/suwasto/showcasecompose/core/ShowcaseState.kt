package io.github.suwasto.showcasecompose.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ShowcaseState {
    private val _steps = mutableStateListOf<ShowcaseStep>()
    val steps: List<ShowcaseStep> get() = _steps

    var currentIndex by mutableStateOf(0)

    val isActive: Boolean
        get() = _steps.isNotEmpty() && currentIndex < _steps.size

    fun addStep(step: ShowcaseStep) { _steps.add(step) }

    fun next() {
        currentIndex++
        if (currentIndex >= _steps.size) end()
    }

    fun end() {
        _steps.clear()
        currentIndex = 0
    }

}