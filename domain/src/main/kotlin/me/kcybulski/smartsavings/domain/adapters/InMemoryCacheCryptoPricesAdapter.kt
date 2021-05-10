package me.kcybulski.smartsavings.domain.adapters

import me.kcybulski.smartsavings.domain.Coin
import me.kcybulski.smartsavings.domain.ports.CryptoPricesPort
import reactor.core.publisher.Flux
import reactor.core.publisher.Flux.fromIterable
import java.math.BigDecimal
import java.time.LocalDate

class InMemoryCacheCryptoPricesAdapter(
    private val cryptoPrices: CryptoPricesPort
) : CryptoPricesPort {

    private val cache: MutableMap<String, Map<LocalDate, BigDecimal>> = mutableMapOf()

    override fun getExchange(
        base: Coin,
        quote: Coin,
        days: List<LocalDate>
    ): Flux<Pair<LocalDate, BigDecimal>> {
        val prices: Map<LocalDate, BigDecimal?> = days
            .associateWith { cache[base.symbol]?.get(it) }

        val cachedValues: List<Pair<LocalDate, BigDecimal>> = prices
            .filter { (_, v) -> v != null }
            .map { (k, v) -> k to v!! }

        val daysToFetch: List<LocalDate> = prices.filter { (_, v) -> v == null }
            .keys
            .toList()
        return cryptoPrices
            .getExchange(base, quote, daysToFetch)
            .doOnNext { cache.merge(base.symbol, mutableMapOf(it)) { a, b -> (a + b).toMutableMap() } }
            .concatWith(fromIterable(cachedValues))
    }
}
