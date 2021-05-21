package me.kcybulski.smartsavings

import me.kcybulski.smartsavings.api.Server
import me.kcybulski.smartsavings.coinpaprika.CoinpaprikaCryptoPricesFactory
import me.kcybulski.smartsavings.domain.SmartSavings
import me.kcybulski.smartsavings.domain.WalletCalculator
import me.kcybulski.smartsavings.redis.RedisCacheCryptoPrices
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import java.time.Clock
import java.time.Clock.systemUTC

fun main() {

    val clock: Clock = systemUTC()
    val cryptoPrices = CoinpaprikaCryptoPricesFactory.create()

    val redissonClient: RedissonClient = Config()
        .also {
            it.useSingleServer()
                .setAddress(System.getenv("REDIS_HOST"))
                .setPassword(System.getenv("REDIS_PASSWORD"))
                .setConnectionMinimumIdleSize(8)
                .setConnectionPoolSize(20)
        }
        .let(Redisson::create)

    redissonClient.keys.flushall()

    val cache = RedisCacheCryptoPrices(redissonClient, cryptoPrices)

    val walletCalculator = WalletCalculator(cache)
    val smartSavings = SmartSavings(clock, walletCalculator)

    Server(smartSavings).start()
}
