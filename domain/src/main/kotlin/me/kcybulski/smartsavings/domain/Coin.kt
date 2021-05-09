package me.kcybulski.smartsavings.domain

data class Coin internal constructor(val symbol: String, val name: String)

class Cryptocurrencies {

    companion object {
        val TETHER: Coin = Coin("USDT", "Tether")
        val BITCOIN: Coin = Coin("BTC", "Bitcoin")
        val ETHEREUM: Coin = Coin("ETH", "Ethereum")
    }

}
