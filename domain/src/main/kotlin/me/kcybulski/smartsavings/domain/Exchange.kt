package me.kcybulski.smartsavings.domain

import java.math.BigDecimal

data class Exchange(
    val base: Coin,
    val quote: Coin,
    val price: BigDecimal
) {
//
//    operator fun plus(another: Exchange): Exchange {
//        require(base == another.base)
//        return of(price + another.price, base)
//    }
//
//    operator fun div(another: Exchange): BigDecimal {
//        require(base == another.base)
//        return price / another.price
//    }
//
//    operator fun times(multiplayer: BigDecimal): Exchange = of(price.multiply(multiplayer), base)
//
//    operator fun times(multiplayer: Int): Exchange = times(multiplayer.toBigDecimal())

}

//fun Collection<Exchange>.sum(): Exchange = reduce { a, b -> a + b }
