package me.kcybulski.smartsavings.domain

import java.math.BigDecimal

data class Earnings(
    val base: Coin,
    val investment: BigDecimal,
    val worth: BigDecimal
)
