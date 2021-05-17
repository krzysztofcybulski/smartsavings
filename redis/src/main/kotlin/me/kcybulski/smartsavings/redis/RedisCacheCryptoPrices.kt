package me.kcybulski.smartsavings.redis

import me.kcybulski.smartsavings.domain.Coin
import me.kcybulski.smartsavings.domain.ports.CryptoPricesPort
import org.redisson.api.RedissonClient
import org.redisson.api.RedissonReactiveClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Flux.concat
import java.math.BigDecimal
import java.time.LocalDate

class RedisCacheCryptoPrices(
    redissonClient: RedissonClient,
    private val cryptoPrices: CryptoPricesPort
) : CryptoPricesPort {

    private val redis: RedissonReactiveClient = redissonClient.reactive()

    override fun getExchange(base: Coin, quote: Coin, days: List<LocalDate>): Flux<Pair<LocalDate, BigDecimal>> {
        val cache = Flux
            .fromIterable(days)
            .flatMap { date -> redis.getBucket<BigDecimal>(key(base, quote, date)).get().map { date to it } }
            .doOnNext { println("Loaded from cache ${base.symbol} ${quote.symbol} ${it.first} -> ${it.second}") }

        val fetched = cache.filter { (day, _) -> day !in days }
            .collectList()
            .map { it.map { it.first } }
            .flatMapMany { cryptoPrices.getExchange(base, quote, it) }
            .doOnNext { persist(base, quote, it.first, it.second) }
            .doOnNext { println("Savings to cache ${base.symbol} ${quote.symbol} ${it.first} -> ${it.second}") }

        return concat(cache, fetched)
    }

    private fun persist(base: Coin, quote: Coin, day: LocalDate, value: BigDecimal) =
        redis
            .getBucket<BigDecimal>(key(base, quote, day))
            .set(value)

    private fun key(base: Coin, quote: Coin, day: LocalDate) = "_ex_${base.symbol}_${quote.symbol}_${day}"

}
