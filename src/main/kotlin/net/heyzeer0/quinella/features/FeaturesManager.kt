package net.heyzeer0.quinella.features

import net.heyzeer0.quinella.features.managers.BlackDesertManager
import net.heyzeer0.quinella.features.managers.ActivityChangerManager

object FeaturesManager {

    fun registerFeatures() {
        BlackDesertManager.registerAlgorithm()
        ActivityChangerManager.startAlgorithm()
    }

}