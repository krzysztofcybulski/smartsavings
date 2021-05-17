package me.kcybulski.smartsavings.api

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import me.kcybulski.smartsavings.coinpaprika.CoinpaprikaCryptoPricesFactory
import me.kcybulski.smartsavings.domain.Asset
import me.kcybulski.smartsavings.domain.Cryptocurrencies.Companion.BITCOIN
import me.kcybulski.smartsavings.domain.Cryptocurrencies.Companion.ETHEREUM
import me.kcybulski.smartsavings.domain.Cryptocurrencies.Companion.TETHER
import me.kcybulski.smartsavings.domain.Earnings
import me.kcybulski.smartsavings.domain.EveryDay
import me.kcybulski.smartsavings.domain.EveryMonth
import me.kcybulski.smartsavings.domain.EveryWeek
import me.kcybulski.smartsavings.domain.Investment
import me.kcybulski.smartsavings.domain.SmartSavings
import me.kcybulski.smartsavings.domain.WalletCalculator
import me.kcybulski.smartsavings.domain.adapters.InMemoryCacheCryptoPricesAdapter
import me.kcybulski.smartsavings.redis.RedisCacheCryptoPrices
import org.redisson.Redisson
import org.redisson.config.Config
import ratpack.reactor.ReactorRatpack
import ratpack.server.RatpackServer
import java.math.BigDecimal.ONE
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDate.now

val clock: Clock = Clock.systemUTC()
val objectMapper: ObjectMapper = ObjectMapper()
    .registerModule(KotlinModule())
    .registerModule(JavaTimeModule())
    .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)

val cryptoPrices = CoinpaprikaCryptoPricesFactory.create()

val redissonClient = Config()
    .also {
        it.useSingleServer()
            .setAddress(System.getenv("REDIS_URL"))
    }
    .let(Redisson::create)

val cache = RedisCacheCryptoPrices(redissonClient, cryptoPrices)

val walletCalculator = WalletCalculator(cache)
val smartSavings = SmartSavings(clock, walletCalculator)

fun main() {
    RatpackServer.start { server ->
        server
            .handlers { chain ->
                chain
                    .get { ctx ->

                        val started = ctx.request.queryParams["since"]
                            ?.let { LocalDate.parse(it) }
                            ?: now().minusMonths(1)

                        val frequency = when (ctx.request.queryParams["frequency"]) {
                            "day" -> EveryDay
                            "week" -> EveryWeek
                            "month" -> EveryMonth
                            else -> EveryMonth
                        }

                        val asset = when (ctx.request.queryParams["coin"]) {
                            "btc" -> BITCOIN
                            "eth" -> ETHEREUM
                            else -> BITCOIN
                        }

                        val investmentMoney = ctx.request.queryParams["investment"]?.toBigDecimal() ?: ONE

                        val investment = Investment(
                            started = started,
                            frequency = frequency,
                            assets = listOf(Asset(asset, investmentMoney)),
                            coin = TETHER
                        )

                        smartSavings.howMuchWorthNow(investment)
                            .let(ReactorRatpack::promiseSingle)
                            .map { Response(investment, it) }
                            .then { response -> ctx.response.send(objectMapper.writeValueAsString(response)) }
                    }
            }
    }
}

class Response(
    val investment: Investment,
    val earnings: Earnings
)
