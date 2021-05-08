package me.kcybulski.smartsavings.domain

import java.math.BigDecimal
import java.time.LocalDate

class Investment(
    val started: LocalDate,
    val frequency: Frequency,
    val assets: List<Asset>
)

data class Asset internal constructor(val crypto: Cryptocurrency, val money: Money)

data class Cryptocurrency internal constructor(val symbol: String)

data class Money(val value: BigDecimal, val currency: Currency)

data class Currency(val iso: String)

class CryptocurrenciesFactory {

    fun bitcoin(): Cryptocurrency = Cryptocurrency("BTC")

}
