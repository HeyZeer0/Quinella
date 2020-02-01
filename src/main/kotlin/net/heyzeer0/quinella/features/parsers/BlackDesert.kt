package net.heyzeer0.quinella.features.parsers

import net.heyzeer0.quinella.core.*
import java.lang.StringBuilder
import java.util.*

object BlackDesert {

    //0 = Saturday | 1 = Monday | 2 = Tuesday | 3 = Wednesday | 4 = Thursday | 5 = Friday | 6 = Sunday -> days
    //0 = 02:00  | 1 = 11:00  | 2 = 16:00   | 3 = 18:00     | 4 = 20:00    | 5 = 23:30 -> bosses
    val bosses = arrayOf(
        "Nouver"          , "Nouver & Kutum"  , "Karanda & Kzarka", "Vell"          , "Karanda & Nouver", "Offin",
        "Karanda & Kzarka", "Nouver"          , "Kutum & Kzarka"  , null            , "Nouver & Kutum"  , "Garmoth",
        "Kutum"           , "Kzarka"          , "Nouver & Kutum"  , null            , "Karanda & Kzarka", "Offin",
        "Kzarka"          , "Kutum & Karanda" , "Kzarka & Nouver" , null            , "Quint & Muraka"  , "Garmoth",
        "Karanda & Kutum" , "Nouver & Karanda", "Kutum & Kzarka"  , null            , "Nouver & Kutum"  , "Offin",
        "Karanda"         , "Nouver"          , "Kutum & Kzarka"  , null            , "Nouver & Kzarka" , "Garmoth",
        "Kzarka"          , "Kzarka & Karanda", "Nouver & Kutum"  , null            , "Quint & Muraka"  , null
    )

    /**
     * Returns the boss name based on the id
     *
     * @param id = variates between 1 ~ 6
     */
    fun getBossById(id: Int): String? {
        return bosses[(getCorrectDate().getDay() * 6) - (7 - id)]
    }

    /**
     * Fixes and return the correct timestamp
     * this is used for getting the next boss
     */
    private fun getCorrectDate(): Calendar {
        val currentDate = Calendar.getInstance(TimeZone.getTimeZone("BET"))

        val toAdd = currentDate.getHours() + (24 - currentDate.getHours())
        if(currentDate.getHours() >= 20 && bosses[(currentDate.getDay() * 6)-1] == null) {
            currentDate.setHours(toAdd)
        }else if(currentDate.getHours() >= 23 && currentDate.getMinutes() >= 30) {
            currentDate.setHours(toAdd)
        }

        return currentDate
    }

    /**
     * Calculates the next boss id
     *
     * @return the boss id, variates from 1 ~ 6
     */
    fun getNextBossId(): Int {
        val date = getCorrectDate()

        var bossId = 1

        if (date.getHours() >= 23 && date.getMinutes() >= 30) bossId = 1
        else if (date.getHours() >= 20) bossId = 6
        else if (date.getHours() >= 18) bossId = 5
        else if (date.getHours() >= 16) bossId = 4
        else if (date.getHours() >= 11) bossId = 3
        else if (date.getHours() >= 2) bossId = 2

        if(getBossById(bossId) == null) {
            if(bossId+1 > 6) bossId = 1
            else bossId += 1
        }

        return bossId
    }

    /**
     * Calculates the boss exactly timestamp in Calendar format
     *
     * @return the calendar instance with the correct date
     */
    fun getBossAsDate(bossId: Int): Calendar {
        val date = getCorrectDate()

        date.setMinutes(0)
        when (bossId) {
            1 -> date.setHours(2)
            2 -> date.setHours(11)
            3 -> date.setHours(16)
            4 -> date.setHours(18)
            5 -> date.setHours(20)
            else -> {
                date.setHours(23)
                date.setMinutes(30)
            }
        }

        return date
    }

    /**
     * Returns a message containing all today bosses
     *
     * @return a message containing all today bosses
     */
    fun getTodayBosses():String {
        val message = StringBuilder()

        for(i in 1..6) {
            val bossName = getBossById(i) ?: continue

            message.append(bossName)
            when(i) {
                1 -> message.append(" (02:00), ")
                2 -> message.append(" (11:00), ")
                3 -> message.append(" (16:00), ")
                4 -> message.append(" (18:00), ")
                5 -> message.append(" (20:00), ")
                6 -> message.append(" (23:30), ")
            }

        }

        return message.toString().dropLast(2)
    }

}