package net.heyzeer0.quinella.core.managers

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object AsyncExecutor {

    private val thread = Executors.newSingleThreadScheduledExecutor()

    fun runAsync(runnable: () -> Unit, delay: Long = 0, unit: TimeUnit = TimeUnit.MILLISECONDS) {
        thread.schedule(runnable, delay, unit)
    }

}