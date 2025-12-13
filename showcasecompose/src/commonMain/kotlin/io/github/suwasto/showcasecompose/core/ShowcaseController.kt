package io.github.suwasto.showcasecompose.core

class ShowcaseController(val state: ShowcaseState) {

    /** Build a list of steps and start showcase */
    fun start(steps: List<ShowcaseStep>) {
        state.end()
        steps.forEach { state.addStep(it) }
        state.currentIndex = 0
    }

    /** Next step */
    fun next() {
        state.next()
    }

    /** End showcase manually */
    fun finish() {
        state.end()
    }

    /** Whether showcase is running */
    val isRunning: Boolean
        get() = state.isActive
}