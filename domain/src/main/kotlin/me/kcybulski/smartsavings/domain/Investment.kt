package me.kcybulski.smartsavings.domain

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.LocalDate
import java.time.Period

class Investment(
    val coin: Coin,
    val assets: List<Asset>,
    private val started: LocalDate,
    private val frequency: Frequency
) {

    fun getDailyInvestments(to: LocalDate): List<DailyInvestment> = frequency
        .buyingDaysBetween(started, to)
        .map { DailyInvestment(it, assets) }

    fun totalInvestment(until: LocalDate): BigDecimal =
        assets.map { it.investment }.fold(ZERO) { a, b -> a + b } * getDays(until).size.toBigDecimal()

    fun getDays(until: LocalDate): List<LocalDate> = frequency.buyingDaysBetween(started, until)

}

data class DailyInvestment(
    val day: LocalDate,
    val assets: List<Asset>
)

data class Asset(val crypto: Coin, val investment: BigDecimal)
