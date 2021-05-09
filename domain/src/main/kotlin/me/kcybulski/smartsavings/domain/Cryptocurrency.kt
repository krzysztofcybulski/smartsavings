package me.kcybulski.smartsavings.domain

data class Cryptocurrency internal constructor(val symbol: String)

class CryptocurrenciesFactory {

    fun bitcoin(): Cryptocurrency = Cryptocurrency("BTC")
    fun ethereum(): Cryptocurrency = Cryptocurrency("ETH")

}
