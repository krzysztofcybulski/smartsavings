package me.kcybulski.smartsavings.support

import kotlin.Pair
import me.kcybulski.smartsavings.domain.CryptoPricesPort
import me.kcybulski.smartsavings.domain.Cryptocurrency
import me.kcybulski.smartsavings.domain.Money

import java.time.LocalDate
import java.util.concurrent.CompletableFuture

import static java.util.concurrent.CompletableFuture.completedFuture

class TestCryptoPrices implements CryptoPricesPort {

    Map<String, List<Pair<LocalDate, Money>>> prices = [:]

    void setPrice(String crypto, LocalDate day, Money price) {
        prices.merge(crypto, [new Pair<LocalDate, Money>(day, price)]) { a, b -> a + b }
    }

    CompletableFuture<Money> getUSDTPriceAt(Cryptocurrency crypto, LocalDate day) {
        List<Pair<LocalDate, Money>> history = prices[crypto.symbol].sort { it.first }
        return completedFuture(history[history.findLastIndexOf { it.first.isBefore(day) }].second)
    }

}
