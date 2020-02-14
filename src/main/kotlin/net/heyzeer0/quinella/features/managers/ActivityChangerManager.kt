package net.heyzeer0.quinella.features.managers

import net.dv8tion.jda.api.entities.Activity
import net.heyzeer0.quinella.core.currentTimeMillis
import net.heyzeer0.quinella.core.randomGame
import net.heyzeer0.quinella.core.runTask
import net.heyzeer0.quinella.jda

object ActivityChangerManager {

    private var nextChange = currentTimeMillis() + 900000 //15 minutes

    fun startAlgorithm() {
        runTask({
            if (currentTimeMillis() < nextChange) return@runTask
            nextChange = currentTimeMillis() + 900000; //15 minutes

            jda.presence.activity = Activity.playing(randomGame()!!)
        }, "ActivityChanger")
    }

}