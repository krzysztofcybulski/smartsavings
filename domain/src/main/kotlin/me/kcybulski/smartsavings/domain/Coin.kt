package me.kcybulski.smartsavings.domain

data class Coin internal constructor(val symbol: String, val name: String)

class Cryptocurrencies {

    companion object {
        val TETHER: Coin = Coin("USDT", "Tether")
        val BITCOIN: Coin = Coin("BTC", "Bitcoin")
        val ETHEREUM: Coin = Coin("ETH", "Ethereum")

        fun fromSymbol(symbol: String): Coin? = when(symbol.lowercase()) {
            "usdt" -> TETHER
            "btc" -> BITCOIN
            "eth" -> ETHEREUM
            else -> null
        }

    }

}
