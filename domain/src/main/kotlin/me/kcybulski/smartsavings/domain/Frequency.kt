package me.kcybulski.smartsavings.domain

import java.time.LocalDate

interface Frequency {

    fun buyingDaysBetween(since: LocalDate, to: LocalDate): List<LocalDate> =
        if (since.isBefore(to))
            listOf(since) + EveryDay.buyingDaysBetween(nextDay(since), to)
        else
            listOf(to)

    fun nextDay(previous: LocalDate): LocalDate

}

object EveryDay : Frequency {

    override fun nextDay(previous: LocalDate): LocalDate = previous.plusDays(1)

}

object EveryWeek : Frequency {

    override fun nextDay(previous: LocalDate): LocalDate = previous.plusWeeks(1)

}

object EveryMonth : Frequency {

    override fun nextDay(previous: LocalDate): LocalDate = previous.plusMonths(1)

}
