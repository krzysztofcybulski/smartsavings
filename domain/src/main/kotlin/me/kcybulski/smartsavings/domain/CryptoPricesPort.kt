package me.kcybulski.smartsavings.domain

import java.time.LocalDate
import java.util.concurrent.CompletableFuture

interface CryptoPricesPort {

    fun getUSDTPriceAt(crypto: Cryptocurrency, day: LocalDate): CompletableFuture<Money>

}
