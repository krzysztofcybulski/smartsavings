package me.kcybulski.smartsavings.support

import kotlin.Pair
import me.kcybulski.smartsavings.domain.Coin
import me.kcybulski.smartsavings.domain.Exchange
import me.kcybulski.smartsavings.domain.ports.CryptoPricesPort
import reactor.core.publisher.Flux

import java.time.LocalDate

class TestCryptoPrices implements CryptoPricesPort {

    Map<Coin, List<Pair<LocalDate, Exchange>>> prices = [:]

    void setPrice(LocalDate day, Exchange exchange) {
        prices.merge(exchange.base, [new Pair<LocalDate, Exchange>(day, exchange)]) { a, b -> a + b }
    }

    Flux<Pair<LocalDate, BigDecimal>> getExchange(Coin base, Coin quote, List<LocalDate> days) {
        return Flux.fromIterable(days)
                .map { day -> new Pair(day, getSingleExchange(base, quote, day).price) }
    }

    private Exchange getSingleExchange(Coin crypto, Coin quote, LocalDate day) {
        List<Pair<LocalDate, Exchange>> history = prices[crypto].sort { it.first }
        def index = history.findLastIndexOf { it.second.quote == quote && it.first.isBefore(day) }
        return history[index].second
    }
}
