package me.kcybulski.smartsavings.domain

import java.math.BigDecimal

data class Money internal constructor(val value: BigDecimal, val currency: Currency) {

    operator fun plus(another: Money): Money {
        require(currency == another.currency)
        return of(value + another.value, currency)
    }

    operator fun div(another: Money): BigDecimal {
        require(currency == another.currency)
        return value / another.value
    }

    operator fun times(multiplayer: BigDecimal): Money = of(value.multiply(multiplayer), currency)

    operator fun times(multiplayer: Int): Money = times(multiplayer.toBigDecimal())

    companion object {

        fun of(value: BigDecimal, currency: Currency): Money = Money(value.setScale(2), currency)
    }
}

fun Collection<Money>.sum(): Money = reduce { a, b -> a + b }

data class Currency(val iso: String)
