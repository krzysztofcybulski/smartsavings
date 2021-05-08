package me.kcybulski.smartsavings.domain

import java.time.LocalDate

interface CryptoPricesPort {

    fun getPriceAt(crypto: Cryptocurrency, day: LocalDate): Money

}
