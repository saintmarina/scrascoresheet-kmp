package com.steelsoftware.scrascoresheet.finished

import com.arkivanov.decompose.ComponentContext

class FinishedComponent(
    componentContext: ComponentContext,
    private val onRestart: () -> Unit
) : ComponentContext by componentContext {

    fun restart() = onRestart()
}