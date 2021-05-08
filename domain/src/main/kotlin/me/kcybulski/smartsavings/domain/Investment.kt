package me.kcybulski.smartsavings.domain

import java.math.BigDecimal
import java.time.Duration.between
import java.time.LocalDate
import java.time.Period

class Investment(
    private val started: LocalDate,
    private val frequency: Frequency,
    private val assets: List<Asset>
) {

    fun getDailyInvestments(to: LocalDate): List<DailyInvestment> = frequency
        .buyingDaysBetween(started, to)
        .map { DailyInvestment(it, assets) }

    fun totalInvestment(to: LocalDate): Money =
        assets.map { it.money }.reduce { acc, asset -> acc + asset } * (Period.between(started, to).days + 1)

}

data class DailyInvestment(
    val day: LocalDate,
    val assets: List<Asset>
)

data class Asset internal constructor(val crypto: Cryptocurrency, val money: Money)

data class Wallet(val entries: Map<Cryptocurrency, BigDecimal>) {

    operator fun plus(another: Wallet): Wallet = (entries.asSequence() + another.entries.asSequence())
        .distinct()
        .groupBy({ it.key }, { it.value })
        .mapValues { (_, values) -> values.reduce { a, b -> a + b } }
        .let { Wallet(it) }

}

data class Cryptocurrency internal constructor(val symbol: String)

data class Money internal constructor(val value: BigDecimal, val currency: Currency) {

    operator fun plus(another: Money): Money {
        require(currency == another.currency)
        return of(value + another.value, currency)
    }

    operator fun div(another: Money): BigDecimal {
        require(currency == another.currency)
        return value / another.value
    }

    operator fun times(multiplayer: BigDecimal): Money = of(value.multiply(multiplayer), currency)

    operator fun times(multiplayer: Int): Money = times(multiplayer.toBigDecimal())

    companion object {

        fun of(value: BigDecimal, currency: Currency) = Money(value.setScale(2), currency)

    }
}

data class Currency(val iso: String)

class CryptocurrenciesFactory {

    fun bitcoin(): Cryptocurrency = Cryptocurrency("BTC")
    fun ethereum(): Cryptocurrency = Cryptocurrency("ETH")

}
