package me.kcybulski.smartsavings.redis

import kotlin.Pair
import me.kcybulski.smartsavings.domain.Coin
import me.kcybulski.smartsavings.domain.ports.CryptoPricesPort
import org.testcontainers.containers.GenericContainer
import org.testcontainers.spock.Testcontainers
import reactor.core.publisher.Flux
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.util.concurrent.AsyncConditions

import java.time.LocalDate

@Testcontainers
class RedisCacheSpec extends Specification {

    @Shared
    GenericContainer redis = new GenericContainer<>('redis:5.0.3-alpine')
            .withExposedPorts(6379)

    def conds = new AsyncConditions()

    CryptoPricesPort cryptoPrices = Mock(CryptoPricesPort)

    @Subject
    RedisCacheCryptoPrices cache = new RedisCacheCryptoPrices(
            new RedisConfiguration(getRedisAddress(), null, 10, 10),
            cryptoPrices
    )

    def setupSpec() {
        redis.start()
    }

    def 'should get values from redis cache'() {
        given:
            Coin coin = new Coin('BTC', 'bitcoin')
            Coin quote = new Coin('USDT', 'tether')
        and:
            LocalDate monday = LocalDate.parse('2021-06-24')
            LocalDate tuesday = LocalDate.parse('2021-06-25')
            LocalDate wednesday = LocalDate.parse('2021-06-26')
        and:
            cryptoPrices.getExchange(coin, quote, [monday, tuesday]) >> Flux.just(
                    new Pair(monday, 100_000.00),
                    new Pair(tuesday, 110_000.00)
            )
            cryptoPrices.getExchange(coin, quote, [wednesday]) >> Flux.just(
                    new Pair(wednesday, 120_000.00)
            )
        when:
            Flux<Pair<LocalDate, BigDecimal>> exchange = cache.getExchange(coin, quote, [monday, tuesday])
        then:
            exchange.collectList().block().toSet() == [
                    new Pair(monday, 100_000.00),
                    new Pair(tuesday, 110_000.00)
            ] as Set
        when:
            Flux<Pair<LocalDate, BigDecimal>> tuesdayExchange = cache.getExchange(coin, quote, [monday, tuesday, wednesday])
        then:
            tuesdayExchange.collectList().block().toSet() == [
                    new Pair(monday, 100_000.00),
                    new Pair(tuesday, 110_000.00),
                    new Pair(wednesday, 120_000.00)
            ] as Set
    }

    def 'cleanupSpec'() {
        redis.stop()
    }

    private String getRedisAddress() {
        return "redis://${redis.host}:${redis.firstMappedPort}"
    }
}
