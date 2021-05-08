package me.kcybulski.smartsavings.domain

import java.time.LocalDate

interface Frequency {

    fun buyingDaysBetween(since: LocalDate, to: LocalDate): List<LocalDate>

}

object EveryDay : Frequency {

    override fun buyingDaysBetween(since: LocalDate, to: LocalDate): List<LocalDate> = TODO()

}

object EveryWeek : Frequency {

    override fun buyingDaysBetween(since: LocalDate, to: LocalDate): List<LocalDate> = TODO()

}

object EveryMonth : Frequency {

    override fun buyingDaysBetween(since: LocalDate, to: LocalDate): List<LocalDate> = TODO()

}
