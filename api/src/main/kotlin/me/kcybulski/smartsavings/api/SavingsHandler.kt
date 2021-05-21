package me.kcybulski.smartsavings.api

import me.kcybulski.smartsavings.api.SavingsHandler.InvalidCoinSymbolException
import me.kcybulski.smartsavings.domain.Asset
import me.kcybulski.smartsavings.domain.Coin
import me.kcybulski.smartsavings.domain.Cryptocurrencies.Companion.fromSymbol
import me.kcybulski.smartsavings.domain.EveryDay
import me.kcybulski.smartsavings.domain.EveryMonth
import me.kcybulski.smartsavings.domain.EveryWeek
import me.kcybulski.smartsavings.domain.Frequency
import me.kcybulski.smartsavings.domain.Investment
import me.kcybulski.smartsavings.domain.SmartSavings
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.jackson.Jackson.json
import ratpack.reactor.ReactorRatpack
import reactor.core.publisher.Flux.fromIterable
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.error
import reactor.core.publisher.Mono.just
import reactor.core.publisher.Mono.zip
import reactor.kotlin.core.publisher.toMono
import reactor.util.function.Tuple3
import java.math.BigDecimal
import java.time.LocalDate

internal class SavingsHandler(
    private val smartSavings: SmartSavings
) : Handler {

    override fun handle(ctx: Context) {
        ctx
            .parse(InvestmentRequest::class.java)
            .let(ReactorRatpack::mono)
            .flatMap(InvestmentRequest::toInvestment)
            .flatMap(smartSavings::howMuchWorthNow)
            .let(ReactorRatpack::promiseSingle)
            .then { ctx.render(json(it)) }
    }

    data class InvestmentRequest(
        val since: LocalDate,
        val frequency: String,
        val coin: String,
        val assets: List<AssetRequest>
    ) {

        fun toInvestment(): Mono<Investment> = zip(
            getAssets(),
            getFrequency(),
            coin.toCoin()
        )
            .map { it.toTriple() }
            .map { (assets, frequency, coin) ->
                Investment(
                    started = since,
                    assets = assets,
                    frequency = frequency,
                    coin = coin
                )
            }

        private fun getAssets(): Mono<List<Asset>> = fromIterable(assets)
            .flatMap { it.toAsset() }
            .collectList()

        private fun getFrequency(): Mono<Frequency> = when (frequency.lowercase()) {
            "day" -> just(EveryDay)
            "week" -> just(EveryWeek)
            "month" -> just(EveryMonth)
            else -> error(InvalidFrequency(frequency))
        }

    }

    data class AssetRequest(
        val coin: String,
        val money: BigDecimal
    ) {
        fun toAsset(): Mono<Asset> = coin.toCoin()
            .map { Asset((it), money) }
    }

    class InvalidFrequency(frequency: String) : RuntimeException("Invalid frequency $frequency")

    class InvalidCoinSymbolException(symbol: String) : RuntimeException("Invalid symbol $symbol")

}

private fun String.toCoin(): Mono<Coin> = fromSymbol(this)?.toMono()
    ?: error(InvalidCoinSymbolException(this))

private fun <A, B, C> Tuple3<A, B, C>.toTriple(): Triple<A, B, C> = Triple(t1, t2, t3)
