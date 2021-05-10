package me.kcybulski.smartsavings.domain

import java.math.BigDecimal

data class Exchange(
    val base: Coin,
    val quote: Coin,
    val price: BigDecimal
)
