package me.kcybulski.smartsavings

import me.kcybulski.smartsavings.api.Server
import me.kcybulski.smartsavings.coinpaprika.CoinpaprikaCryptoPricesFactory
import me.kcybulski.smartsavings.domain.SmartSavings
import me.kcybulski.smartsavings.domain.WalletCalculator
import me.kcybulski.smartsavings.redis.RedisCacheCryptoPrices
import me.kcybulski.smartsavings.redis.RedisConfiguration
import java.lang.System.getenv
import java.time.Clock
import java.time.Clock.systemUTC

fun main() {

    val clock: Clock = systemUTC()
    val redisConfig = RedisConfiguration(getenv("REDIS_HOST"), getenv("REDIS_PASSWORD"))

    val cryptoPrices = CoinpaprikaCryptoPricesFactory.create()
    val cache = RedisCacheCryptoPrices(redisConfig, cryptoPrices)
    val walletCalculator = WalletCalculator(cache)
    val smartSavings = SmartSavings(clock, walletCalculator)

    Server(smartSavings).start()
}
