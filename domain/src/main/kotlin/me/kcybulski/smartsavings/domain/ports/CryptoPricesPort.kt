package me.kcybulski.smartsavings.domain.ports

import me.kcybulski.smartsavings.domain.Coin
import me.kcybulski.smartsavings.domain.Exchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.LocalDate
import java.util.concurrent.CompletableFuture

interface CryptoPricesPort {

    fun getExchange(base: Coin, quote: Coin, days: List<LocalDate>): Flux<Pair<LocalDate, BigDecimal>>

}
