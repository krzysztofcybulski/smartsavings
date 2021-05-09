package me.kcybulski.smartsavings

import me.kcybulski.smartsavings.domain.Coin
import me.kcybulski.smartsavings.domain.Cryptocurrencies
import me.kcybulski.smartsavings.domain.Exchange

import java.time.LocalDate

import static java.time.LocalDate.ofEpochDay

class TestData {

    static LocalDate randomDay() {
        return ofEpochDay(20000)
    }

    static Coin TETHER = Cryptocurrencies.@Companion.TETHER
    static Coin BITCOIN = Cryptocurrencies.@Companion.BITCOIN
    static Coin ETHEREUM = Cryptocurrencies.@Companion.ETHEREUM

    static Exchange bitcoinWorth(BigDecimal price) {
        return new Exchange(BITCOIN, TETHER, price)
    }

    static Exchange ethereumWorth(BigDecimal price) {
        return new Exchange(ETHEREUM, TETHER, price)
    }

}
