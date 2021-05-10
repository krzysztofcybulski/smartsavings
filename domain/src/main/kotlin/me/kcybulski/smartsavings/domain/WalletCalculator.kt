package me.kcybulski.smartsavings.domain

import me.kcybulski.smartsavings.domain.Wallet.Companion.emptyWallet
import me.kcybulski.smartsavings.domain.Wallet.Companion.singleAssetWallet
import me.kcybulski.smartsavings.domain.ports.CryptoPricesPort
import reactor.core.publisher.Flux.fromIterable
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode.CEILING
import java.time.LocalDate

class WalletCalculator(
    private val cryptoPrices: CryptoPricesPort
) {

    fun fromInvestment(investment: Investment, day: LocalDate): Mono<Wallet> {
        val days = investment.getDays(day)
        return fromIterable(investment.assets)
            .flatMap { asset ->
                cryptoPrices
                    .getExchange(asset.crypto, investment.coin, days)
                    .map { asset.investment.divide(it.second, it.second.scale(), CEILING) }
                    .reduce(ZERO) { a, b -> a + b }
                    .map { singleAssetWallet(asset.crypto, it) }
            }
            .reduce(emptyWallet()) { a, b -> a + b }
    }

    fun walletWorth(wallet: Wallet, quote: Coin, day: LocalDate): Mono<BigDecimal> {
        return fromIterable(wallet.entries.entries)
            .flatMap { (coin, amount) ->
                cryptoPrices
                    .getExchange(coin, quote, listOf(day))
                    .map { it.second * amount }
            }
            .reduce(ZERO) { a, b -> a + b }
    }
}
