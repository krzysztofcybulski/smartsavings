package me.kcybulski.smartsavings.coinpaprika

import me.kcybulski.smartsavings.domain.Coin
import me.kcybulski.smartsavings.domain.ports.CryptoPricesPort
import reactor.core.publisher.Flux
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDate

internal class CoinpaprikaCryptoPricesAdapter(
    private val client: CoinpaprikaClient
) : CryptoPricesPort {

    override fun getExchange(
        base: Coin,
        quote: Coin,
        days: List<LocalDate>
    ): Flux<Pair<LocalDate, BigDecimal>> {
        require(quote.symbol == "USDT")
        return days
            .groupBy { it.year }
            .let { Flux.fromIterable(it.values) }
            .flatMap { d -> client.getDayPrices(coinId(base), d.first().year) }
            .filter { it.date in days }
            .map { it.date to it.startingPrice }
    }

    private fun coinId(coin: Coin) = "${coin.symbol}-${coin.name}".lowercase()

}
