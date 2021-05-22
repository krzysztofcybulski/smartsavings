package me.kcybulski.smartsavings.redis

import me.kcybulski.smartsavings.domain.Coin
import me.kcybulski.smartsavings.domain.ports.CryptoPricesPort
import org.redisson.Redisson
import org.redisson.api.RedissonReactiveClient
import org.redisson.config.Config
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.LocalDate

class RedisCacheCryptoPrices(
    private val redisConfiguration: RedisConfiguration,
    private val cryptoPrices: CryptoPricesPort
) : CryptoPricesPort {

    private val redis: RedissonReactiveClient = Config()
        .also {
            it.useSingleServer()
                .setAddress(redisConfiguration.address)
                .setPassword(redisConfiguration.password)
                .setConnectionMinimumIdleSize(redisConfiguration.connectionMinimumIdleSize)
                .setConnectionPoolSize(redisConfiguration.connectionPoolSize)
        }
        .let(Redisson::create)
        .reactive()

    override fun getExchange(base: Coin, quote: Coin, days: List<LocalDate>): Flux<Pair<LocalDate, BigDecimal>> {
        val cache = Flux
            .fromIterable(days)
            .flatMap { date -> redis.getBucket<BigDecimal>(key(base, quote, date)).get().map { date to it } }
            .doOnNext { println("Loading from cache ${base.symbol} ${quote.symbol} ${it.first} -> ${it.second}") }
            .cache()

        return cache.collectList()
            .map { cached -> days - cached.map { it.first } }
            .flatMapMany { cryptoPrices.getExchange(base, quote, it) }
            .flatMap { persist(base, quote, it.first, it.second) }
            .concatWith(cache)
    }

    fun invalidate(): Mono<Void> = redis
        .keys
        .flushdbParallel()

    private fun persist(base: Coin, quote: Coin, day: LocalDate, value: BigDecimal): Mono<Pair<LocalDate, BigDecimal>> =
        redis
            .getBucket<BigDecimal>(key(base, quote, day))
            .set(value)
            .thenReturn(day to value)
            .doOnNext { println("Saving to cache ${base.symbol} ${quote.symbol} $day -> $value") }

    private fun key(base: Coin, quote: Coin, day: LocalDate) = "_ex_${base.symbol}_${quote.symbol}_${day}"

}
