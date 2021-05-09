package me.kcybulski.smartsavings.domain

import java.math.BigDecimal
import java.time.LocalDate
import java.util.concurrent.CompletableFuture

class WalletCalculator(
    private val cryptoPrices: CryptoPricesPort
) {

    fun fromInvestment(investment: Investment, day: LocalDate): CompletableFuture<Wallet> = investment
        .getDailyInvestments(day)
        .map { dailyCryptoWallet(it) }
        .allOf()
        .thenApply { it.reduce { acc, wallet -> acc + wallet } }

    fun walletWorth(wallet: Wallet, day: LocalDate): CompletableFuture<Money> = wallet.entries
            .mapValues { (key, value) -> cryptoPrices.getUSDTPriceAt(key, day).thenApply { it * value } }
            .values
            .allOf()
            .thenApply { it.sum() }

    private fun dailyCryptoWallet(dailyInvestment: DailyInvestment): CompletableFuture<Wallet> =
        dailyInvestment.assets
            .map { howMuchCrypto(it, dailyInvestment.day) }
            .allOf()
            .thenApply { Wallet(it.toMap()) }

    private fun howMuchCrypto(asset: Asset, day: LocalDate): CompletableFuture<Pair<Cryptocurrency, BigDecimal>> =
        cryptoPrices.getUSDTPriceAt(asset.crypto, day)
            .thenApply { asset.crypto to asset.money / it }

}

fun <T> Collection<CompletableFuture<T>>.allOf(): CompletableFuture<List<T>> = CompletableFuture.allOf(*toTypedArray())
    .thenApply { map { it.join() } }
