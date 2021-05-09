package me.kcybulski.smartsavings.domain

import java.math.BigDecimal

data class Wallet(val entries: Map<Coin, BigDecimal>) {

    operator fun plus(another: Wallet): Wallet = (entries.asSequence() + another.entries.asSequence())
        .distinct()
        .groupBy({ it.key }, { it.value })
        .mapValues { (_, values) -> values.reduce { a, b -> a + b } }
        .let { Wallet(it) }

    companion object {

        fun singleAssetWallet(coin: Coin, amount: BigDecimal): Wallet = Wallet(mapOf(coin to amount))
        fun emptyWallet(): Wallet = Wallet(emptyMap())

    }
}
